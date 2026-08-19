/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.repositoryservices.postgres.repositoryconnector.database;

import org.odpi.openmetadata.adapters.repositoryservices.postgres.repositoryconnector.ffdc.PostgresErrorCode;
import org.odpi.openmetadata.adapters.repositoryservices.postgres.repositoryconnector.schema.RepositoryColumn;
import org.odpi.openmetadata.adapters.repositoryservices.postgres.repositoryconnector.schema.RepositoryTable;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.MatchCriteria;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.AttributeTypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.PrimitiveDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.PrimitiveDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefAttribute;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Translates open metadata query requests into SQL fragments that can be assembled before issuing
 * then to the database.
 */
public class QueryBuilder
{
    private final OMRSRepositoryHelper  repositoryHelper;
    private final String                repositoryName;


    private String                relationshipEndGUID          = null;

    private List<String>          end1EntityGUIDs              = null;
    private List<String>          end2EntityGUIDs              = null;
    private EndMatchCriteria      endMatchCriteria             = null;

    private String                searchString                 = null;
    private boolean               startsWith                   = false;
    private boolean               endsWith                     = false;
    private boolean               ignoreCase                   = true;

    private SearchProperties      searchProperties             = null;
    private String                principleTableName           = null;
    private String                propertyTableName            = null;
    private SearchClassifications matchClassifications         = null;
    private List<String>          limitResultsByClassification = null;
    private String                typeGUID                     = null;
    private String                typeGUIDParameterName        = "typeGUID";
    private List<String>          subtypeGUIDs                 = null;
    private String                subTypeGUIDsParameterName    = "subTypeGUIDs";
    private boolean               skipSubtypes                 = false;
    private List<InstanceStatus>  limitResultsByStatus         = null;
    private List<String>          guidList                     = null;
    private Date                  asOfTime                     = null;
    private String                sequencingProperty           = null;
    private SequencingOrder       sequencingOrder              = null;
    private int                   fromElement                  = 0;
    private int                   pageSize                     = 0;


    private static final Logger log = LoggerFactory.getLogger(QueryBuilder.class);


    /**
     * Constructor.
     *
     * @param principleTableName name of entity, relationship, classification
     * @param propertyTableName name of entity_attribute_value, classification_attribute_value, relationship_attribute_value
     * @param repositoryName name of this repository
     * @param repositoryHelper helper
     */
    public QueryBuilder(String               principleTableName,
                        String               propertyTableName,
                        OMRSRepositoryHelper repositoryHelper,
                        String               repositoryName)
    {
        this.principleTableName = principleTableName;
        this.propertyTableName = propertyTableName;
        this.repositoryHelper = repositoryHelper;
        this.repositoryName   = repositoryName;
    }


    /**
     * Set up an entity GUID for searching for an entity's relationships.
     *
     * @param relationshipEndGUID entity GUID
     */
    public void setRelationshipEndGUID(String relationshipEndGUID)
    {
        this.relationshipEndGUID = relationshipEndGUID;
    }


    /**
     * Derive the SQL fragment that
     *
     * @return fragment of SQL
     */
    private String getRelationshipEndGUIDClause()
    {
        if (relationshipEndGUID != null)
        {
            return " and (" + RepositoryColumn.END_1_GUID.getColumnName() + " = '" + escapePropertyValue(relationshipEndGUID) +
                    "' or " + RepositoryColumn.END_2_GUID.getColumnName() + " = '" + escapePropertyValue(relationshipEndGUID) + "')";
        }

        return " ";
    }


    /**
     * Derive a SQL fragment that restricts a count query to only the instances that the named repository is
     * responsible for counting: those it homes itself (metadataCollectionId matches) and those it has been
     * assigned to count on behalf of a non-cohort provenance (replicatedBy matches).  This avoids double-counting
     * instances that are replicated across more than one member of a cohort when a federated count sums the
     * results from each repository.
     * <br><br>
     * This method must only be called when building the WHERE clause for the countEntities()/countRelationships()
     * queries - it is deliberately kept separate from getAsOfTimeWhereClause() (used by every other query,
     * including findEntities()/findRelationships()) so that every other query continues to return every stored
     * matching instance, including replicas, for federation to deduplicate by GUID.
     *
     * @param localMetadataCollectionId unique identifier of the repository issuing the count query
     * @return fragment of SQL
     */
    public String getLocalMetadataCollectionClause(String localMetadataCollectionId)
    {
        if (localMetadataCollectionId != null)
        {
            return " and (" + RepositoryColumn.METADATA_COLLECTION_GUID.getColumnName() + " = '" + escapePropertyValue(localMetadataCollectionId) +
                    "' or " + RepositoryColumn.REPLICATED_BY.getColumnName() + " = '" + escapePropertyValue(localMetadataCollectionId) + "')";
        }

        return " ";
    }

    /**
     * Set up the Java regular expression used to match against any of the String property values
     * within instances of the specified type(s).
     *
     * @param searchString regex
     * @param startsWith true if the search should be for strings that start with the search string
     * @param endsWith true if the search should be for strings that end with the search string
     * @param ignoreCase true if the search should be case-insensitive
     */
    public void setSearchString(String  searchString,
                                boolean startsWith,
                                boolean endsWith,
                                boolean ignoreCase)
    {
        this.searchString = searchString;
        this.startsWith   = startsWith;
        this.endsWith     = endsWith;
        this.ignoreCase   = ignoreCase;
    }


    /**
     * Return the SQL search string that needs to appear in the SQL query.
     *
     * @return fragment of SQL
     */
    private String getSearchStringClause()
    {
        if (searchString != null)
        {
            String searchOperand = " like ";
            if (ignoreCase)
            {
                searchOperand = " ilike ";
            }

            StringBuilder searchStringBuilder = new StringBuilder();
            if (! startsWith)
            {
                searchStringBuilder.append("%");
            }

            searchStringBuilder.append(getSafeLikePattern(searchString));

            if (! endsWith)
            {
                searchStringBuilder.append("%");
            }

            return " and " + getPropertySubSelect(null,
                                                  null,
                                                  searchOperand,
                                                  searchStringBuilder.toString(),
                                                  principleTableName,
                                                  propertyTableName);
        }

        return " ";
    }


    /**
     * Creates a sub-select statement that returns a list of guids that have properties matching the desired property value
     * Property name or property value can be null but not both.  The operator is required if property value is not null.
     * The property column name is required in property name is not null
     *
     * @param propertyName name of the property to test (or null for any property)
     * @param propertyColumn is the property name an attribute name or a nested property name?
     * @param operator operator to compare the property value
     * @param propertyValue property value to look for (already validated and escaped).
     * @param principleTableName name of header table
     * @param propertyTableName name of attribute table
     * @return sub select statement
     */
    private String getPropertySubSelect(String propertyName,
                                        String propertyColumn,
                                        String operator,
                                        String propertyValue,
                                        String principleTableName,
                                        String propertyTableName)
    {
        String subSelect  = " (" + RepositoryColumn.INSTANCE_GUID.getColumnName(principleTableName) +
                                   " in (select " + RepositoryColumn.INSTANCE_GUID.getColumnName(propertyTableName) + " from " + propertyTableName +
                                          " where (";

        if (propertyName != null)
        {
            subSelect = subSelect + propertyColumn + "='" + escapePropertyValue(propertyName) + "'";

            if (propertyValue != null)
            {
                subSelect = subSelect + " and ";
            }
        }

        if (propertyValue != null)
        {
            subSelect = subSelect + RepositoryColumn.PROPERTY_VALUE.getColumnName() + " " + operator + "  '" + escapePropertyValue(propertyValue) + "'";
        }

        return subSelect + "))) ";
    }


    /**
     * Ensure any single quote in a property value is escaped.
     *
     * @param propertyValue supplied property value
     * @return escaped property value
     */
    private String escapePropertyValue(Object propertyValue)
    {
        if (propertyValue != null)
        {
            return propertyValue.toString().replaceAll("'", "''");
        }

        return null;
    }


    /**
     * Escape the characters that PostgreSQL's LIKE/ILIKE operators treat as special, so that a value
     * supplied by the caller only ever matches itself: the two wildcards ("%" matches any run of
     * characters, "_" matches any single character) and the backslash that escapes them.
     * <br><br>
     * The backslash matters in both directions.  Left alone, it swallows whatever character follows it -
     * so a search for "C:\temp" would silently look for "C:temp" and find nothing - and a value that
     * ends in a backslash leaves the pattern ending in a lone escape character, which PostgreSQL rejects
     * outright ("LIKE pattern must not end with escape character") rather than simply not matching.
     * <br><br>
     * This is independent of {@link #escapePropertyValue(Object)}, which handles the separate job of
     * making a value safe to embed in a SQL string literal: neither escaper introduces characters the
     * other one acts on, so a value needing both can have them applied in either order.
     *
     * @param suppliedSearchString the string to escape so that it is matched literally
     * @return string that is safe to use as (part of) a LIKE pattern
     */
    private String getSafeLikePattern(Object suppliedSearchString)
    {
        if (suppliedSearchString != null)
        {
            StringBuilder searchStringBuilder = new StringBuilder();
            for (int i = 0; i < suppliedSearchString.toString().length(); i++)
            {
                if ((suppliedSearchString.toString().charAt(i) == '%') ||
                    (suppliedSearchString.toString().charAt(i) == '_') ||
                    (suppliedSearchString.toString().charAt(i) == '\\'))
                {
                    searchStringBuilder.append('\\');
                }
                searchStringBuilder.append(suppliedSearchString.toString().charAt(i));
            }

            return searchStringBuilder.toString();
        }

        return null;
    }


    /**
     * Set up the properties that should be matched during the query.
     *
     * @param matchProperties Optional list of entity properties to match (where any String property's value should
     *                        be defined as a Java regular expression, even if it should be an exact match).
     * @param matchCriteria Enum defining how the match properties should be matched to the entities in the repository.
     */
    public void setMatchProperties(InstanceProperties matchProperties,
                                   MatchCriteria      matchCriteria)
    {
        if ((matchProperties != null) &&
                (matchProperties.getPropertyCount() > 0 ||
                        matchProperties.getEffectiveFromTime() != null ||
                        matchProperties.getEffectiveToTime() != null))
        {
            SearchProperties        searchProperties = new SearchProperties();
            List<PropertyCondition> propertyConditions = new ArrayList<>();

            if (matchProperties.getEffectiveFromTime() != null)
            {
                PropertyCondition propertyCondition = this.getEffectiveTimePropertyCondition(matchCriteria,
                                                                                             OpenMetadataProperty.EFFECTIVE_FROM_TIME.name,
                                                                                             matchProperties.getEffectiveFromTime());
                propertyConditions.add(propertyCondition);
            }

            if (matchProperties.getEffectiveToTime() != null)
            {
                PropertyCondition propertyCondition = this.getEffectiveTimePropertyCondition(matchCriteria,
                                                                                             OpenMetadataProperty.EFFECTIVE_TO_TIME.name,
                                                                                             matchProperties.getEffectiveToTime());
                propertyConditions.add(propertyCondition);
            }

            if (matchProperties.getPropertyCount() > 0)
            {
                Map<String, InstancePropertyValue> properties = matchProperties.getInstanceProperties();

                for (String propertyName : properties.keySet())
                {
                    InstancePropertyValue instancePropertyValue = properties.get(propertyName);
                    PropertyCondition     propertyCondition     = new PropertyCondition();

                    propertyCondition.setProperty(propertyName);
                    propertyCondition.setValue(instancePropertyValue);

                    if (matchCriteria == MatchCriteria.NONE)
                    {
                        propertyCondition.setOperator(PropertyComparisonOperator.NEQ);
                    }
                    else
                    {
                        propertyCondition.setOperator(PropertyComparisonOperator.EQ);
                    }

                    propertyConditions.add(propertyCondition);
                }
            }

            searchProperties.setConditions(propertyConditions);
            searchProperties.setMatchCriteria(matchCriteria);

            this.setSearchProperties(searchProperties);
        }
    }


    /**
     * Return an encoding of an effective time - this stores the value as a date rather than the usual long for
     * date attributes.
     *
     * @param matchCriteria Enum defining how the match properties should be matched to the entities in the repository.
     * @param propertyName name of property
     * @param dateValue value to use
     * @return property condition
     */
    private PropertyCondition getEffectiveTimePropertyCondition(MatchCriteria matchCriteria,
                                                                String        propertyName,
                                                                Date          dateValue)
    {
        PropertyCondition propertyCondition = new PropertyCondition();

        propertyCondition.setProperty(propertyName);

        if (matchCriteria == MatchCriteria.NONE)
        {
            propertyCondition.setOperator(PropertyComparisonOperator.NEQ);
        }
        else
        {
            propertyCondition.setOperator(PropertyComparisonOperator.EQ);
        }

        PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();

        primitivePropertyValue.setHeaderVersion(InstancePropertyValue.CURRENT_INSTANCE_PROPERTY_VALUE_HEADER_VERSION);
        primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_DATE);
        primitivePropertyValue.setTypeName(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_DATE.getName());
        primitivePropertyValue.setTypeGUID(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_DATE.getGUID());
        primitivePropertyValue.setPrimitiveValue(dateValue);

        return propertyCondition;
    }


    /**
     * Step through the hierarchy of properties, building out the nested clauses of the search query.
     *
     * @param instanceProperties collection of properties to work on (most will be primitives)
     * @param topLevelPropertyName parent attribute name - not null when dealing with nested properties
     * @param stringPropertyOperator how to compare the property value stored with the property value supplied.
     * @param numericPropertyOperator how to compare the property value stored with the property value supplied.
     * @param matchOperand how to combine the results from different properties
     * @param principleTableName name of header table
     * @param propertyTableName name of attribute table
     * @return sql fragment wrapped in parentheses.  Forms part of a where clause
     */
    private String getPropertyComparisonFromInstanceProperties(InstanceProperties         instanceProperties,
                                                               String                     topLevelPropertyName,
                                                               PropertyComparisonOperator stringPropertyOperator,
                                                               PropertyComparisonOperator numericPropertyOperator,
                                                               String                     matchOperand,
                                                               String                     principleTableName,
                                                               String                     propertyTableName) throws RepositoryErrorException
    {
        if ((instanceProperties != null) && (instanceProperties.getPropertyCount() > 0))
        {
            StringBuilder stringBuilder = new StringBuilder(" (");

            Iterator<String> propertyNames = instanceProperties.getPropertyNames();
            boolean          firstProperty = true;

            while (propertyNames.hasNext())
            {
                String leafPropertyName = propertyNames.next();

                InstancePropertyValue instancePropertyValue = instanceProperties.getPropertyValue(leafPropertyName);

                if (instancePropertyValue != null)
                {
                    if (firstProperty)
                    {
                        firstProperty = false;
                    }
                    else
                    {
                        stringBuilder.append(matchOperand);
                    }
                }

                if (instancePropertyValue instanceof PrimitivePropertyValue primitivePropertyValue)
                {
                    if (primitivePropertyValue.getPrimitiveDefCategory() == PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING)
                    {
                        stringBuilder.append(this.getNestedPropertyComparisonClause(topLevelPropertyName,
                                                                                    leafPropertyName,
                                                                                    stringPropertyOperator,
                                                                                    this.escapePropertyValue(primitivePropertyValue.getPrimitiveValue()),
                                                                                    principleTableName,
                                                                                    propertyTableName));
                    }
                    else
                    {
                        stringBuilder.append(this.getNestedPropertyComparisonClause(topLevelPropertyName,
                                                                                    leafPropertyName,
                                                                                    numericPropertyOperator,
                                                                                    primitivePropertyValue.getPrimitiveValue(),
                                                                                    principleTableName,
                                                                                    propertyTableName));
                    }
                }
                else if (instancePropertyValue instanceof EnumPropertyValue enumPropertyValue)
                {
                    stringBuilder.append(this.getNestedPropertyComparisonClause(topLevelPropertyName,
                                                                                leafPropertyName,
                                                                                stringPropertyOperator,
                                                                                this.escapePropertyValue(enumPropertyValue.getSymbolicName()),
                                                                                principleTableName,
                                                                                propertyTableName));
                }
                else if (instancePropertyValue instanceof MapPropertyValue mapPropertyValue)
                {
                    stringBuilder.append(getPropertyComparisonFromInstanceProperties(mapPropertyValue.getMapValues(),
                                                                                     leafPropertyName,
                                                                                     stringPropertyOperator,
                                                                                     numericPropertyOperator,
                                                                                     matchOperand,
                                                                                     principleTableName,
                                                                                     propertyTableName));
                }
                else if (instancePropertyValue instanceof ArrayPropertyValue arrayPropertyValue)
                {
                    stringBuilder.append(getPropertyComparisonFromInstanceProperties(arrayPropertyValue.getArrayValues(),
                                                                                     leafPropertyName,
                                                                                     stringPropertyOperator,
                                                                                     numericPropertyOperator,
                                                                                     matchOperand,
                                                                                     principleTableName,
                                                                                     propertyTableName));
                }
                else if (instancePropertyValue instanceof StructPropertyValue structPropertyValue)
                {
                    stringBuilder.append(getPropertyComparisonFromInstanceProperties(structPropertyValue.getAttributes(),
                                                                                     leafPropertyName,
                                                                                     stringPropertyOperator,
                                                                                     numericPropertyOperator,
                                                                                     matchOperand,
                                                                                     principleTableName,
                                                                                     propertyTableName));
                }
            }

            stringBuilder.append(") ");

            return stringBuilder.toString();
        }

        return " ";
    }


    /**
     * Generate the clause for a particular property
     *
     * @param topLevelPropertyName name of top level property name
     * @param leafPropertyName name of leaf property to look for
     * @param operator operator
     * @param propertyValue value to look for
     * @param principleTableName name of header table
     * @param propertyTableName name of attribute table
     * @return sql fragment
     * @throws RepositoryErrorException the property does not make sense with the operator
     */
    private String getNestedPropertyComparisonClause(String                     topLevelPropertyName,
                                                     String                     leafPropertyName,
                                                     PropertyComparisonOperator operator,
                                                     Object                     propertyValue,
                                                     String                     principleTableName,
                                                     String                     propertyTableName) throws RepositoryErrorException
    {
        final String methodName = "getNestedPropertyComparisonClause";

        String propertyColumn = this.mapPropertyNameToColumn(leafPropertyName, RepositoryColumn.ATTRIBUTE_NAME.getColumnName());

        if (propertyColumn.equals(RepositoryColumn.ATTRIBUTE_NAME.getColumnName()) || propertyColumn.equals(RepositoryColumn.PROPERTY_NAME.getColumnName()))
        {
            String propertyNameMatchClause = this.getPropertyNameMatchClause(propertyTableName,
                                                                             topLevelPropertyName,
                                                                             leafPropertyName);
            String rowMatchClause = "select 1 from " + propertyTableName +
                    " where " + RepositoryColumn.INSTANCE_GUID.getColumnName(principleTableName) + " = " + RepositoryColumn.INSTANCE_GUID.getColumnName(propertyTableName) +
                    " and " + RepositoryColumn.VERSION.getColumnName(principleTableName) + " = " + RepositoryColumn.VERSION.getColumnName(propertyTableName);

            if (operator == PropertyComparisonOperator.IS_NULL)
            {
                if (propertyTableName != null)
                {
                    if (propertyNameMatchClause == null)
                    {
                        return " not exists (" + rowMatchClause + ") ";
                    }
                    else
                    {
                        return " not exists (" + rowMatchClause + " and " + propertyNameMatchClause + ") ";
                    }
                }
            }
            else
            {
                String sqlClause;

                if (propertyNameMatchClause == null)
                {
                    sqlClause = "exists (" + rowMatchClause;
                }
                else
                {
                    sqlClause = " exists (" + rowMatchClause + " and " + propertyNameMatchClause;
                }

                switch (operator)
                {
                    case EQ ->
                    {
                        return sqlClause + " and " + RepositoryColumn.PROPERTY_VALUE.getColumnName() + " = '" + propertyValue + "') ";
                    }
                    case NEQ ->
                    {
                        return sqlClause + " and " + RepositoryColumn.PROPERTY_VALUE.getColumnName() + " != '" + propertyValue + "') ";
                    }
                    case LT ->
                    {
                        return sqlClause + " and " + RepositoryColumn.PROPERTY_VALUE.getColumnName() + " < '" + propertyValue + "') ";
                    }
                    case LTE ->
                    {
                        return sqlClause + " and " + RepositoryColumn.PROPERTY_VALUE.getColumnName() + " <= '" + propertyValue + "') ";
                    }
                    case GT ->
                    {
                        return sqlClause + " and " + RepositoryColumn.PROPERTY_VALUE.getColumnName() + " > '" + propertyValue + "') ";
                    }
                    case GTE ->
                    {
                        return sqlClause + " and " + RepositoryColumn.PROPERTY_VALUE.getColumnName() + " >= '" + propertyValue + "') ";
                    }
                    case LIKE ->
                    {
                        return sqlClause + " and " + RepositoryColumn.PROPERTY_VALUE.getColumnName() + " like '%" + this.getSafeLikePattern(propertyValue) + "%') ";
                    }
                    case NOT_LIKE ->
                    {
                        return sqlClause + " and " + RepositoryColumn.PROPERTY_VALUE.getColumnName() + " not like '%" + this.getSafeLikePattern(propertyValue) + "%') ";
                    }
                    case CASE_INSENSITIVE_LIKE ->
                    {
                        return sqlClause + " and " + RepositoryColumn.PROPERTY_VALUE.getColumnName() + " ilike '%" + this.getSafeLikePattern(propertyValue) + "%') ";
                    }
                    case CASE_INSENSITIVE_NOT_LIKE ->
                    {
                        return sqlClause + " and " + RepositoryColumn.PROPERTY_VALUE.getColumnName() + " not ilike '%" + this.getSafeLikePattern(propertyValue) + "%') ";
                    }
                    case STARTS_WITH ->
                    {
                        return sqlClause + " and " + RepositoryColumn.PROPERTY_VALUE.getColumnName() + " like '" + this.getSafeLikePattern(propertyValue) + "%') ";
                    }
                    case ENDS_WITH ->
                    {
                        return sqlClause + " and " + RepositoryColumn.PROPERTY_VALUE.getColumnName() + " like '%" + this.getSafeLikePattern(propertyValue) + "') ";
                    }
                    case CASE_INSENSITIVE_STARTS_WITH ->
                    {
                        return sqlClause + " and " + RepositoryColumn.PROPERTY_VALUE.getColumnName() + " ilike '" + this.getSafeLikePattern(propertyValue) + "%') ";
                    }
                    case CASE_INSENSITIVE_ENDS_WITH ->
                    {
                        return sqlClause + " and " + RepositoryColumn.PROPERTY_VALUE.getColumnName() + " ilike '%" + this.getSafeLikePattern(propertyValue) + "') ";
                    }
                    case CASE_INSENSITIVE_EQ ->
                    {
                        return sqlClause + " and " + RepositoryColumn.PROPERTY_VALUE.getColumnName() + " ilike '" + this.getSafeLikePattern(propertyValue) + "') ";
                    }
                    case NOT_NULL ->
                    {
                        return sqlClause + ") ";
                    }
                }
            }
        }
        else // property in a dedicated column
        {
            switch (operator)
            {
                case EQ ->
                {
                    return " (" + principleTableName + "." + propertyColumn + " = '" + propertyValue + "') ";
                }
                case NEQ ->
                {
                    return " (" + principleTableName + "." + propertyColumn + " != '" + propertyValue + "') ";
                }
                case LT ->
                {
                    return " (" + principleTableName + "." + propertyColumn + " < '" + propertyValue + "') ";
                }
                case LTE ->
                {
                    return " (" + principleTableName + "." + propertyColumn + " <= '" + propertyValue + "') ";
                }
                case GT ->
                {
                    return " (" + principleTableName + "." + propertyColumn + " > '" + propertyValue + "') ";
                }
                case GTE ->
                {
                    return " (" + principleTableName + "." + propertyColumn + " >= '" + propertyValue + "') ";
                }
                case IS_NULL ->
                {
                    return " (" + principleTableName + "." + propertyColumn + " is null) ";
                }
                case NOT_NULL ->
                {
                    return " (" + principleTableName + "." + propertyColumn + " is not null) ";
                }
                case LIKE ->
                {
                    return " (" + principleTableName + "." + propertyColumn + " like '%" + this.getSafeLikePattern(propertyValue) + "%') ";
                }
                case NOT_LIKE ->
                {
                    return " (" + principleTableName + "." + propertyColumn + " not like '%" + this.getSafeLikePattern(propertyValue) + "%') ";
                }
                case CASE_INSENSITIVE_LIKE ->
                {
                    return " (" + principleTableName + "." + propertyColumn + " ilike '%" + this.getSafeLikePattern(propertyValue) + "%') ";
                }
                case CASE_INSENSITIVE_NOT_LIKE ->
                {
                    return " (" + principleTableName + "." + propertyColumn + " not ilike '%" + this.getSafeLikePattern(propertyValue) + "%') ";
                }
                case STARTS_WITH ->
                {
                    return " (" + principleTableName + "." + propertyColumn + " like '" + this.getSafeLikePattern(propertyValue) + "%') ";
                }
                case ENDS_WITH ->
                {
                    return " (" + principleTableName + "." + propertyColumn + " like '%" + this.getSafeLikePattern(propertyValue) + "') ";
                }
                case CASE_INSENSITIVE_STARTS_WITH ->
                {
                    return " (" + principleTableName + "." + propertyColumn + " ilike '" + this.getSafeLikePattern(propertyValue) + "%') ";
                }
                case CASE_INSENSITIVE_ENDS_WITH ->
                {
                    return " (" + principleTableName + "." + propertyColumn + " ilike '%" + this.getSafeLikePattern(propertyValue) + "') ";
                }
                case CASE_INSENSITIVE_EQ ->
                {
                    return " (" + principleTableName + "." + propertyColumn + " ilike '" + this.getSafeLikePattern(propertyValue) + "') ";
                }
            }
        }

        throw new RepositoryErrorException(PostgresErrorCode.BAD_SEARCH_PROPERTY.getMessageDefinition(repositoryName,
                                                                                                      operator.getName(),
                                                                                                      propertyValue.toString()),
                                           this.getClass().getName(),
                                           methodName);
    }


    /**
     * Construct the cluse in the SQL that matches the property name.  This may be a simple
     * primitive name (topLevelName==null) or a nested property in a complex attribute type like a map.
     *
     * @param propertyTableName table to search
     * @param topLevelPropertyName name of the top-level attribute or null
     * @param leafPropertyName name of the requested property
     * @return SQL fragment
     */
    private String getPropertyNameMatchClause(String propertyTableName,
                                              String topLevelPropertyName,
                                              String leafPropertyName)
    {
        if (topLevelPropertyName == null)
        {
            if (leafPropertyName == null)
            {
                return null;
            }
            else
            {
                return RepositoryColumn.ATTRIBUTE_NAME.getColumnName(propertyTableName) + " = '" + escapePropertyValue(leafPropertyName) + "'";
            }
        }
        else
        {
            return RepositoryColumn.ATTRIBUTE_NAME.getColumnName(propertyTableName) + " = '" + escapePropertyValue(topLevelPropertyName) + "' and " +
                   RepositoryColumn.PROPERTY_NAME.getColumnName(propertyTableName) + " like '%:" + escapePropertyValue(getSafeLikePattern(leafPropertyName)) + "'";
        }
    }


    /**
     * Capture the criteria for matching the ends in a findRelationship search.
     *
     * @param end1EntityGUIDs optional list of entity guids used to match end 1 of the relationships.
     * @param end2EntityGUIDs optional list of entity guids used to match end 2 of the relationships.
     * @param endMatchCriteria criteria for matching the ends of the relationships.
     */
    public void setRelationshipEndCriteria(List<String>     end1EntityGUIDs,
                                           List<String>     end2EntityGUIDs,
                                           EndMatchCriteria endMatchCriteria)
    {
        this.end1EntityGUIDs = end1EntityGUIDs;
        this.end2EntityGUIDs = end2EntityGUIDs;
        this.endMatchCriteria = endMatchCriteria;
    }


    /**
     * Derive the SQL fragment to describe the relationship end criteria.
     *
     * @return SQL fragment or null if no criteria
     */
    private String getRelationshipEndCriteriaClause()
    {
        if (endMatchCriteria != null)
        {
            String endMatchOperand = " and ";
            String guidMatchOperand = " or ";
            String matchComparison = " = ";

            if (endMatchCriteria == EndMatchCriteria.ANY)
            {
                endMatchOperand = " or ";
            }
            else if (endMatchCriteria == EndMatchCriteria.NONE)
            {
                matchComparison = " != ";
                guidMatchOperand = " and ";
            }

            StringBuilder stringBuilder = new StringBuilder();

            if (end1EntityGUIDs != null)
            {
                stringBuilder.append(" (");

                boolean firstEnd1GUID = true;

                for (String end1EntityGUID : end1EntityGUIDs)
                {
                    if (firstEnd1GUID)
                    {
                        firstEnd1GUID = false;
                    }
                    else
                    {
                        stringBuilder.append(guidMatchOperand);
                    }

                    stringBuilder.append(RepositoryColumn.END_1_GUID.getColumnName());
                    stringBuilder.append(matchComparison);
                    stringBuilder.append("'");
                    stringBuilder.append(end1EntityGUID);
                    stringBuilder.append("'");
                }

                stringBuilder.append(") ");

                if (end2EntityGUIDs != null)
                {
                    stringBuilder.append(endMatchOperand);
                }
            }

            if (end2EntityGUIDs != null)
            {
                stringBuilder.append(" (");

                boolean firstEnd2GUID = true;

                for (String end2EntityGUID : end2EntityGUIDs)
                {
                    if (firstEnd2GUID)
                    {
                        firstEnd2GUID = false;
                    }
                    else
                    {
                        stringBuilder.append(guidMatchOperand);
                    }

                    stringBuilder.append(RepositoryColumn.END_2_GUID.getColumnName());
                    stringBuilder.append(matchComparison);
                    stringBuilder.append("'");
                    stringBuilder.append(end2EntityGUID);
                    stringBuilder.append("'");
                }

                stringBuilder.append(") ");
            }

            if (! stringBuilder.isEmpty())
            {
                return " and (" + stringBuilder + ") ";
            }

            return " ";
        }

        return " ";
    }


    /**
     * Set up the search properties.
     *
     * @param searchProperties Optional list of entity property conditions to match.
     */
    public void setSearchProperties(SearchProperties searchProperties)
    {
        this.searchProperties = searchProperties;
    }


    /**
     * Derive the SQL fragment to describe the search properties.
     * This method searches the principle table for matching properties in the property table.
     *
     * @param principleTableName name of table holding the header
     * @param propertyTableName name of table holding the properties
     * @param searchProperties properties to search for
     * @return fragment of SQL
     */
    private String getSearchPropertiesClause(String           principleTableName,
                                             String           propertyTableName,
                                             SearchProperties searchProperties) throws RepositoryErrorException
    {
        if (searchProperties != null)
        {
            String searchPropertiesClause = this.getPropertyComparisonFromPropertyConditions(searchProperties,
                                                                                             principleTableName,
                                                                                             propertyTableName,
                                                                                             null);

            if (searchPropertiesClause.contains("("))
            {
                return " and " + searchPropertiesClause;
            }

        }

        return " ";
    }


    /**
     * Step through the hierarchy of properties, building out the nested clauses of the search query.
     *
     * @param searchProperties collection of properties to work on (most will be primitives)
     * @param principleTableName name of table holding the header
     * @param propertyTableName name of table holding the properties
     * @param topLevelPropertyName parent attribute name - not null when dealing with nested properties
     * @return sql fragment wrapped in parentheses.  Forms part of a where clause
     */
    private String getPropertyComparisonFromPropertyConditions(SearchProperties searchProperties,
                                                               String           principleTableName,
                                                               String           propertyTableName,
                                                               String           topLevelPropertyName) throws RepositoryErrorException
    {
        if ((searchProperties != null) && (searchProperties.getConditions() != null) && (! searchProperties.getConditions().isEmpty()))
        {
            String matchOperand = " and ";

            if (searchProperties.getMatchCriteria() == MatchCriteria.ANY)
            {
                matchOperand = " or ";
            }

            StringBuilder stringBuilder = new StringBuilder();
            boolean       firstProperty = true;

            for (PropertyCondition propertyCondition : searchProperties.getConditions())
            {
                if (propertyCondition.getNestedConditions() != null)
                {
                    if (firstProperty)
                    {
                        stringBuilder.append(" (");
                        firstProperty = false;
                    }
                    else
                    {
                        stringBuilder.append(matchOperand);
                    }

                    stringBuilder.append(this.getPropertyComparisonFromPropertyConditions(propertyCondition.getNestedConditions(),
                                                                                          principleTableName,
                                                                                          propertyTableName,
                                                                                          topLevelPropertyName));
                }
                else
                {
                    String leafPropertyName = propertyCondition.getProperty();

                    InstancePropertyValue instancePropertyValue = propertyCondition.getValue();

                    if (firstProperty)
                    {
                        stringBuilder.append(" (");
                        firstProperty = false;
                    }
                    else
                    {
                        stringBuilder.append(matchOperand);
                    }

                    if (instancePropertyValue instanceof PrimitivePropertyValue primitivePropertyValue)
                    {
                        if (primitivePropertyValue.getPrimitiveDefCategory() == PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING)
                        {
                            stringBuilder.append(this.getNestedPropertyComparisonClause(topLevelPropertyName,
                                                                                        leafPropertyName,
                                                                                        propertyCondition.getOperator(),
                                                                                        this.escapePropertyValue(primitivePropertyValue.getPrimitiveValue()),
                                                                                        principleTableName,
                                                                                        propertyTableName));
                        }
                        else
                        {
                            stringBuilder.append(this.getNestedPropertyComparisonClause(topLevelPropertyName,
                                                                                        leafPropertyName,
                                                                                        propertyCondition.getOperator(),
                                                                                        primitivePropertyValue.getPrimitiveValue(),
                                                                                        principleTableName,
                                                                                        propertyTableName));
                        }
                    }
                    else if (instancePropertyValue instanceof EnumPropertyValue enumPropertyValue)
                    {
                        stringBuilder.append(this.getNestedPropertyComparisonClause(topLevelPropertyName,
                                                                                    leafPropertyName,
                                                                                    propertyCondition.getOperator(),
                                                                                    this.escapePropertyValue(enumPropertyValue.getSymbolicName()),
                                                                                    principleTableName,
                                                                                    propertyTableName));
                    }
                    else if (instancePropertyValue instanceof MapPropertyValue mapPropertyValue)
                    {
                        stringBuilder.append(getPropertyComparisonFromInstanceProperties(mapPropertyValue.getMapValues(),
                                                                                         leafPropertyName,
                                                                                         propertyCondition.getOperator(),
                                                                                         propertyCondition.getOperator(),
                                                                                         matchOperand,
                                                                                         principleTableName,
                                                                                         propertyTableName));
                    }
                    else if (instancePropertyValue instanceof ArrayPropertyValue arrayPropertyValue)
                    {
                        stringBuilder.append(getPropertyComparisonFromInstanceProperties(arrayPropertyValue.getArrayValues(),
                                                                                         leafPropertyName,
                                                                                         propertyCondition.getOperator(),
                                                                                         propertyCondition.getOperator(),
                                                                                         matchOperand,
                                                                                         principleTableName,
                                                                                         propertyTableName));
                    }
                    else if (instancePropertyValue instanceof StructPropertyValue structPropertyValue)
                    {
                        stringBuilder.append(getPropertyComparisonFromInstanceProperties(structPropertyValue.getAttributes(),
                                                                                         leafPropertyName,
                                                                                         propertyCondition.getOperator(),
                                                                                         propertyCondition.getOperator(),
                                                                                         matchOperand,
                                                                                         principleTableName,
                                                                                         propertyTableName));
                    }
                    else // null property value
                    {
                        stringBuilder.append(this.getNestedPropertyComparisonClause(topLevelPropertyName,
                                                                                    leafPropertyName,
                                                                                    propertyCondition.getOperator(),
                                                                                    null,
                                                                                    principleTableName,
                                                                                    propertyTableName));
                    }
                }
            }

            stringBuilder.append(") ");

            return stringBuilder.toString();
        }

        return " ";
    }


    /**
     * Set up an optional list of entity classifications to match.
     *
     * @param matchClassifications match classifications
     */
    public void setSearchClassifications(SearchClassifications matchClassifications)
    {
        this.matchClassifications = matchClassifications;
    }


    /**
     * Derive the SQL fragment to describe the search classifications.
     *
     * @return fragment of SQL
     */
    private String getSearchClassificationsClause() throws RepositoryErrorException
    {
        if ((matchClassifications != null) && (matchClassifications.getConditions() != null))
        {
            StringBuilder stringBuilder  = new StringBuilder();
            StringBuilder conditionsBuilder = new StringBuilder();
            boolean       firstCondition  = true;

            /*
             * MatchCriteria was previously ignored here: every named classification condition was
             * unconditionally AND-ed onto the classification-table join, so a query naming two or
             * more mutually exclusive classifications (e.g. ZoneMembership OR Confidentiality) always
             * returned zero rows regardless of whether the caller asked for ALL, ANY, or NONE. Mirror
             * the pattern used for property conditions in getPropertyComparisonFromPropertyConditions()
             * (ANY -> "or", ALL -> "and") and wrap the whole group in "not (...)" for NONE.
             */
            String matchOperand = " and ";

            if (matchClassifications.getMatchCriteria() == MatchCriteria.ANY
                    || matchClassifications.getMatchCriteria() == MatchCriteria.NONE)
            {
                matchOperand = " or ";
            }

            for (ClassificationCondition classificationCondition : matchClassifications.getConditions())
            {
                if (classificationCondition != null)
                {
                    /*
                     * Add in a type clause for the classification, even if the classification name is null to
                     * make the construction of the SQL easier.
                     */
                    if (firstCondition)
                    {
                        conditionsBuilder.append(" (");
                        firstCondition = false;
                    }
                    else
                    {
                        conditionsBuilder.append(matchOperand);
                    }

                    conditionsBuilder.append("(");
                    conditionsBuilder.append(RepositoryColumn.TYPE_NAME.getColumnName(RepositoryTable.CLASSIFICATION.getTableName()));
                    conditionsBuilder.append(" like '%:");

                    if (classificationCondition.getName() != null)
                    {
                        conditionsBuilder.append(classificationCondition.getName());
                    }
                    else
                    {
                        conditionsBuilder.append("%");
                    }
                    conditionsBuilder.append(":%' ");

                    if (classificationCondition.getMatchProperties() != null)
                    {
                        conditionsBuilder.append(this.getSearchPropertiesClause(RepositoryTable.CLASSIFICATION.getTableName(),
                                                                                RepositoryTable.CLASSIFICATION_ATTRIBUTE_VALUE.getTableName(),
                                                                                classificationCondition.getMatchProperties()));
                    }

                    conditionsBuilder.append(") ");
                }
            }

            if (!firstCondition)
            {
                conditionsBuilder.append(") ");

                stringBuilder.append(" and ");
                if (matchClassifications.getMatchCriteria() == MatchCriteria.NONE)
                {
                    stringBuilder.append("not ");
                }
                stringBuilder.append(conditionsBuilder);
            }

            return stringBuilder.toString();
        }

        return " ";
    }


    /**
     * Set up the list of classifications that must be present on all returned entities.
     *
     * @param limitResultsByClassification list of classification names
     */
    public void setLimitResultsByClassification(List<String> limitResultsByClassification)
    {
        this.limitResultsByClassification = limitResultsByClassification;
    }


    /**
     * Convert the list of classifications that must be present on all returned entities into a SQL fragment.
     *
     * @return fragment of SQL
     */
    private String getLimitResultsByClassificationClaus()
    {
        if ((limitResultsByClassification != null) && (! limitResultsByClassification.isEmpty()))
        {
            StringBuilder stringBuilder = new StringBuilder(" and (");
            boolean       firstClassification = true;

            for (String classificationName : limitResultsByClassification)
            {
                if (firstClassification)
                {
                    firstClassification = false;
                }
                else
                {
                    stringBuilder.append(" or ");
                }

                stringBuilder.append(RepositoryColumn.TYPE_NAME.getColumnName(RepositoryTable.CLASSIFICATION.getTableName()));
                stringBuilder.append(" like '%:");
                stringBuilder.append(escapePropertyValue(getSafeLikePattern(classificationName)));
                stringBuilder.append(":%'");
            }

            stringBuilder.append(")");

            return stringBuilder.toString();
        }


        return " ";
    }


    /**
     * Set up the type information for the query.
     *
     * @param typeGUID unique identifier of desired type.
     * @param typeGUIDParameterName parameter name use to pass the type guid
     */
    public void setTypeGUID(String typeGUID,
                            String typeGUIDParameterName)
    {
        this.typeGUID = typeGUID;
        this.typeGUIDParameterName = typeGUIDParameterName;
    }


    /**
     * Set up the type information for the query.
     *
     * @param typeGUID unique identifier of desired type.
     * @param typeGUIDParameterName parameter name use to pass the type guid
     * @param subTypeGUIDs list of unique identifiers for the desired type
     * @param subTypeGUIDsParameterName parameter name use to pass the subtype guid list
     */
    public void setTypeGUID(String       typeGUID,
                            String       typeGUIDParameterName,
                            List<String> subTypeGUIDs,
                            String       subTypeGUIDsParameterName)
    {
        this.setTypeGUID(typeGUID, typeGUIDParameterName, subTypeGUIDs, false, subTypeGUIDsParameterName);
    }


    /**
     * Set up the type information for the query.
     *
     * @param typeGUID unique identifier of desired type.
     * @param typeGUIDParameterName parameter name use to pass the type guid
     * @param subTypeGUIDs list of unique identifiers for the subtypes to include in (or, if skipSubtypes is true,
     *                     exclude from) the query results.
     * @param skipSubtypes if true, subTypeGUIDs is treated as the list of subtypes to exclude from the query
     *                     results rather than the only subtypes to include.  Ignored if subTypeGUIDs is null or empty.
     * @param subTypeGUIDsParameterName parameter name use to pass the subtype guid list
     */
    public void setTypeGUID(String       typeGUID,
                            String       typeGUIDParameterName,
                            List<String> subTypeGUIDs,
                            boolean      skipSubtypes,
                            String       subTypeGUIDsParameterName)
    {
        this.typeGUID = typeGUID;
        this.typeGUIDParameterName = typeGUIDParameterName;
        this.subtypeGUIDs = subTypeGUIDs;
        this.skipSubtypes = skipSubtypes;
        this.subTypeGUIDsParameterName = subTypeGUIDsParameterName;
    }


    /**
     * Return the SQL fragment that describes the type(s) to search for.
     *
     * @return fragment of SQL
     * @throws RepositoryErrorException invalid type
     */
    private String getTypeClause() throws RepositoryErrorException
    {
        if ((subtypeGUIDs != null) && (! subtypeGUIDs.isEmpty()))
        {
            StringBuilder stringBuffer = new StringBuilder();

            if (skipSubtypes)
            {
                if (typeGUID != null)
                {
                    stringBuffer.append(" and (");
                    stringBuffer.append(RepositoryColumn.TYPE_NAME.getColumnName());
                    stringBuffer.append(" like '%:");
                    stringBuffer.append(this.lookUpTypeName(typeGUID, typeGUIDParameterName));
                    stringBuffer.append(":%')");
                }

                stringBuffer.append(" and not (");
            }
            else
            {
                stringBuffer.append(" and (");
            }

            boolean firstType = true;

            for (String subTypeGUID: subtypeGUIDs)
            {
                if (firstType)
                {
                    firstType = false;
                }
                else
                {
                    stringBuffer.append(" or ");
                }

                stringBuffer.append(RepositoryColumn.TYPE_NAME.getColumnName());
                stringBuffer.append(" like '%:");
                stringBuffer.append(this.lookUpTypeName(subTypeGUID, subTypeGUIDsParameterName));
                stringBuffer.append(":%' ");
            }
            stringBuffer.append(")");

            return stringBuffer.toString();
        }
        else if (typeGUID != null)
        {
            return " and (" + RepositoryColumn.TYPE_NAME.getColumnName() + " like '%:" + this.lookUpTypeName(typeGUID, typeGUIDParameterName) + ":%')";
        }

        return " ";
    }


    /**
     * Convert a typeDefGUID into a typeDefName.
     *
     * @param typeGUID guid of open metadata type
     * @param parameterName name of parameter passing the typeDefGUID
     * @return name of open metadata type.
     */
    private String lookUpTypeName(String typeGUID,
                                  String parameterName) throws RepositoryErrorException
    {
        final String methodName = "lookUpTypeName";

        try
        {
            TypeDef typeDef = repositoryHelper.getTypeDef(repositoryName,
                                                          parameterName,
                                                          typeGUID,
                                                          methodName);

            if (typeDef != null)
            {
                return typeDef.getName();
            }
        }
        catch (TypeErrorException error)
        {
            throw new RepositoryErrorException(PostgresErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(repositoryName,
                                                                                                           error.getClass().getName(),
                                                                                                           methodName,
                                                                                                           error.getMessage()),
                                               this.getClass().getName(),
                                               methodName,
                                               error);
        }

        /*
         * Unreachable
         */
        return OpenMetadataType.OPEN_METADATA_ROOT.typeName;
    }


    /**
     * Set up the list od current statuses that an instance must have to be returned.
     *
     * @param limitResultsByStatus list of statuses
     */
    public void setLimitResultsByStatus(List<InstanceStatus> limitResultsByStatus)
    {
        this.limitResultsByStatus = limitResultsByStatus;
    }


    /**
     * Determine the sql needed to limit the return results.
     *
     * @return fragment of SQL
     */
    private String getLimitResultsByStatusClause()
    {
        if (limitResultsByStatus == null)
        {
            return " and (" + RepositoryColumn.CURRENT_STATUS.getColumnName() + " != '" + InstanceStatus.DELETED.getName() + "') ";
        }
        else if (limitResultsByStatus.isEmpty())
        {
            return " ";
        }
        else
        {
            StringBuilder sqlClause = new StringBuilder(" and (");
            boolean firstStatus = true;

            for (InstanceStatus instanceStatus : limitResultsByStatus)
            {
                if (instanceStatus != null)
                {
                    if (firstStatus)
                    {
                        firstStatus = false;
                    }
                    else
                    {
                        sqlClause.append(" or ");
                    }

                    sqlClause.append(RepositoryColumn.CURRENT_STATUS.getColumnName());
                    sqlClause.append(" = '");
                    sqlClause.append(instanceStatus.getName());
                    sqlClause.append("'");
                }
            }

            sqlClause.append(")");

            return sqlClause.toString();
        }
    }


    /**
     * Set up the database time to issue the query for - null means the latest version
     *
     * @param asOfTime date or null
     */
    public void setAsOfTime(Date asOfTime)
    {
        this.asOfTime = asOfTime;
    }


    /**
     * Create the part of the where clause that ensures that the correct version is returned.
     *
     * @return fragment of SQL
     */
    private String getAsOfTimeClause()
    {
        if (asOfTime == null)
        {
            return " (" + RepositoryColumn.VERSION_END_TIME.getColumnName() + " is null) ";
        }
        else
        {
            // java.util.Date's own toString() (what plain string concatenation of a Date would produce here)
            // has no sub-second precision at all, unlike the millisecond-precision timestamps this same
            // asOfTime is compared against (version_start_time/version_end_time) - wrapping in
            // java.sql.Timestamp keeps the millisecond component in the generated SQL literal.
            String asOfTimeLiteral = new java.sql.Timestamp(asOfTime.getTime()).toString();

            return " (" + RepositoryColumn.VERSION_START_TIME.getColumnName() + " < '" + asOfTimeLiteral + "' and (" + RepositoryColumn.VERSION_END_TIME.getColumnName() + " is null or " + RepositoryColumn.VERSION_END_TIME.getColumnName() + " > '" + asOfTimeLiteral + "')) ";
        }
    }


    /**
     * Set up the sequencing order required.
     *
     * @param sequencingOrder order to return results
     * @param sequencingProperty optional property if sequencing on specific property results
     */
    public void setSequencingOrder(SequencingOrder sequencingOrder,
                                   String          sequencingProperty)
    {
        this.sequencingOrder = sequencingOrder;
        this.sequencingProperty = sequencingProperty;
    }


    /**
     * Return the ORDER BY fragment.  Notice that ordering by property is currently ignored
     *
     * @return sequencing
     */
    private String getSequencingOrder(String principleTableName)
    {
        if (sequencingOrder != null)
        {
            switch (sequencingOrder)
            {
                case ANY, CREATION_DATE_RECENT ->
                {
                    return " order by " + RepositoryColumn.CREATE_TIME.getColumnName(principleTableName) + " desc ";
                }
                case CREATION_DATE_OLDEST ->
                {
                    return " order by " + RepositoryColumn.CREATE_TIME.getColumnName(principleTableName) + " asc ";
                }
                case LAST_UPDATE_RECENT ->
                {
                    return " order by " + RepositoryColumn.UPDATE_TIME.getColumnName(principleTableName) + " desc ";
                }
                case LAST_UPDATE_OLDEST ->
                {
                    return " order by " + RepositoryColumn.UPDATE_TIME.getColumnName(principleTableName) + " asc ";
                }
                case GUID ->
                {
                    return " order by " + RepositoryColumn.INSTANCE_GUID.getColumnName(principleTableName) + " asc ";
                }
                case PROPERTY_DESCENDING ->
                {
                    return " order by " + this.getSequencingPropertyOrderClause(principleTableName) + " desc ";
                }
                case PROPERTY_ASCENDING ->
                {
                    return " order by " + this.getSequencingPropertyOrderClause(principleTableName) + " asc ";
                }
            }
        }

        return " ";
    }


    /**
     * Return the SQL fragment to sort by for PROPERTY_ASCENDING/PROPERTY_DESCENDING sequencing - either a
     * dedicated header column (for the small set of properties that map onto one, eg guid, metadataCollectionId -
     * see mapPropertyNameToColumn()) or a correlated subquery that looks up the requested property's value from
     * this query's property table (eg entity_attribute_value) for the current row's instance/version. This
     * mirrors the "property in a dedicated column" vs "EAV property" split that getNestedPropertyComparisonClause()
     * already uses for property comparisons in the WHERE clause - the same sequencingProperty name has to resolve
     * to the same place it would be compared against.
     * <br><br>
     * The subquery is capped with "limit 1": sequencingProperty is expected to name a single-valued property
     * (its EAV row's attribute_name and property_name coincide), but an array/map/struct property would have
     * several property_table rows sharing that attribute_name, and a scalar subquery that could return more than
     * one row would fail the whole query at execution time rather than degrade gracefully.
     * <br><br>
     * property_value is stored as text regardless of the property's real type (the same is true of every other
     * property comparison in this class, eg the LT/GT operators in getNestedPropertyComparisonClause()). Left
     * alone this would sort a numeric or date property lexicographically rather than by numeric/chronological
     * value (eg "10" sorting before "9"), so the subquery result is cast to numeric whenever sequencingProperty's
     * declared type is one of the numeric primitives - see isNumericProperty().
     *
     * @param principleTableName main table that the ordering will occur on
     * @return SQL fragment naming or computing the value to sort on - does not include "order by" or asc/desc
     */
    private String getSequencingPropertyOrderClause(String principleTableName)
    {
        if (sequencingProperty == null)
        {
            /*
             * No property named to sort by - fall back to creation date rather than producing invalid SQL.
             */
            return RepositoryColumn.CREATE_TIME.getColumnName(principleTableName);
        }

        String propertyColumn = this.mapPropertyNameToColumn(sequencingProperty, null);

        if (RepositoryColumn.ATTRIBUTE_NAME.getColumnName().equals(propertyColumn))
        {
            String subSelect = "(select " + RepositoryColumn.PROPERTY_VALUE.getColumnName() + " from " + propertyTableName +
                    " where " + RepositoryColumn.INSTANCE_GUID.getColumnName(propertyTableName) + " = " + RepositoryColumn.INSTANCE_GUID.getColumnName(principleTableName) +
                    " and " + RepositoryColumn.VERSION.getColumnName(propertyTableName) + " = " + RepositoryColumn.VERSION.getColumnName(principleTableName) +
                    " and " + RepositoryColumn.ATTRIBUTE_NAME.getColumnName(propertyTableName) + " = '" + escapePropertyValue(sequencingProperty) + "'" +
                    " limit 1)";

            if (this.isNumericProperty(sequencingProperty))
            {
                return "cast(" + subSelect + " as numeric)";
            }

            return subSelect;
        }

        return principleTableName + "." + propertyColumn;
    }


    /**
     * Determine whether sequencingProperty is declared with a numeric primitive type (byte, short, int, long,
     * float, double, biginteger, bigdecimal, or date - date properties are stored as epoch-millisecond longs,
     * see RepositoryMapper.extractValuesFromInstanceAuditHeader()) on the type this query is filtering to.  All
     * of these are stored in property_value as their plain numeric string form (Number.toString()), so ordering
     * by them can use a numeric rather than lexical (text) comparison.
     * <br><br>
     * Returns false - lexical ordering, the pre-existing behaviour - if the property's type can't be determined
     * (no type filter was supplied on this query, the type lookup fails, or the property isn't declared on that
     * type) rather than guessing; a query should never fail just because numeric sequencing could not be confirmed.
     *
     * @param propertyName name of the property that sequencing is requested on
     * @return true if the property's declared type is one of the numeric primitives
     */
    private boolean isNumericProperty(String propertyName)
    {
        if (typeGUID == null)
        {
            return false;
        }

        final String methodName = "isNumericProperty";

        try
        {
            TypeDef typeDef = repositoryHelper.getTypeDef(repositoryName, typeGUIDParameterName, typeGUID, methodName);

            if (typeDef == null)
            {
                return false;
            }

            List<TypeDefAttribute> typeDefAttributes = repositoryHelper.getAllPropertiesForTypeDef(repositoryName, typeDef, methodName);

            if (typeDefAttributes != null)
            {
                for (TypeDefAttribute typeDefAttribute : typeDefAttributes)
                {
                    if ((typeDefAttribute != null) && propertyName.equals(typeDefAttribute.getAttributeName()))
                    {
                        AttributeTypeDef attributeTypeDef = typeDefAttribute.getAttributeType();

                        if (attributeTypeDef instanceof PrimitiveDef primitiveDef)
                        {
                            return switch (primitiveDef.getPrimitiveDefCategory())
                            {
                                case OM_PRIMITIVE_TYPE_BYTE, OM_PRIMITIVE_TYPE_SHORT, OM_PRIMITIVE_TYPE_INT,
                                     OM_PRIMITIVE_TYPE_LONG, OM_PRIMITIVE_TYPE_FLOAT, OM_PRIMITIVE_TYPE_DOUBLE,
                                     OM_PRIMITIVE_TYPE_BIGINTEGER, OM_PRIMITIVE_TYPE_BIGDECIMAL, OM_PRIMITIVE_TYPE_DATE -> true;
                                default -> false;
                            };
                        }

                        return false;
                    }
                }
            }
        }
        catch (TypeErrorException error)
        {
            // Fall through to lexical ordering.
        }

        return false;
    }


    /**
     * Determine which column to search to retrieve values for a particular property.
     *
     * @param propertyName name of property
     * @param defaultColumnName default value for attribute property
     * @return column name
     */
    private String mapPropertyNameToColumn(String propertyName, String defaultColumnName)
    {
        if (OpenMetadataProperty.GUID.name.equals(propertyName))
        {
            return RepositoryColumn.INSTANCE_GUID.getColumnName();
        }
        else if (OpenMetadataProperty.METADATA_COLLECTION_ID.name.equals(propertyName))
        {
            return RepositoryColumn.METADATA_COLLECTION_GUID.getColumnName();
        }
        else if (OpenMetadataProperty.METADATA_COLLECTION_NAME.name.equals(propertyName))
        {
            return RepositoryColumn.METADATA_COLLECTION_NAME.getColumnName();
        }
        else if (OpenMetadataProperty.INSTANCE_PROVENANCE_TYPE.name.equals(propertyName))
        {
            return RepositoryColumn.INSTANCE_PROVENANCE_TYPE.getColumnName();
        }
        else if (OpenMetadataProperty.LAST_REQUEST_ID.name.equals(propertyName))
        {
            return RepositoryColumn.LAST_REQUEST_ID.getColumnName();
        }
        else if (OpenMetadataProperty.CREATED_BY.name.equals(propertyName))
        {
            return RepositoryColumn.CREATED_BY.getColumnName();
        }
        else if (OpenMetadataProperty.UPDATED_BY.name.equals(propertyName))
        {
            return RepositoryColumn.UPDATED_BY.getColumnName();
        }
        else if (OpenMetadataProperty.STATUS.name.equals(propertyName))
        {
            return RepositoryColumn.CURRENT_STATUS.getColumnName();
        }
        else if (OpenMetadataProperty.OPEN_METADATA_TYPE_NAME.name.equals(propertyName))
        {
            return RepositoryColumn.TYPE_NAME.getColumnName();
        }
        else if (OpenMetadataProperty.VERSION.name.equals(propertyName))
        {
            return RepositoryColumn.VERSION.getColumnName();
        }
        else if (OpenMetadataProperty.EFFECTIVE_FROM_TIME.name.equals(propertyName))
        {
            return RepositoryColumn.EFFECTIVE_FROM_TIME.getColumnName();
        }
        else if (OpenMetadataProperty.EFFECTIVE_TO_TIME.name.equals(propertyName))
        {
            return RepositoryColumn.EFFECTIVE_TO_TIME.getColumnName();
        }
        else if (defaultColumnName != null)
        {
            return defaultColumnName;
        }

        return RepositoryColumn.ATTRIBUTE_NAME.getColumnName();
    }


    /**
     * Set up the required paging.
     *
     * @param fromElement starting from element (0 for first)
     * @param pageSize maximum  elements that can be returned
     */
    public void setPaging(int fromElement,
                          int pageSize)
    {
        this.fromElement = fromElement;
        this.pageSize = pageSize;
    }


    /**
     * Return the paging requirements for the query.
     *
     * @param principleTableName main table for ordering
     * @return paging
     */
    private String getPaging(String principleTableName)
    {
        if (pageSize == 0)
        {
            return " ";
        }
        else
        {
            String sqlClause = "";

            /*
             * It is not valid to add paging if ordering is not specified.
             */
            if (sequencingOrder == null)
            {
                sequencingOrder = SequencingOrder.CREATION_DATE_RECENT;
                sqlClause = this.getSequencingOrder(principleTableName);
            }

            return sqlClause + " limit " + pageSize +  " offset " + fromElement;
        }
    }


    /**
     * Set up a list of GUIDs as part of a search.
     *
     * @param guidList list of GUID to search for
     */
    public void setGUIDList(List<String> guidList)
    {
        this.guidList = guidList;
    }


    /**
     * Derive the SQL clause that searches for a list of guids.
     *
     * @return SQL command fragment
     */
    private String getGUIDListClause()
    {
        if ((guidList != null) && (! guidList.isEmpty()))
        {
            StringBuilder stringBuilder = new StringBuilder(" and (");
            boolean       firstGUID = true;

            for (String guid : guidList)
            {
                if (firstGUID)
                {
                    firstGUID = false;
                }
                else
                {
                    stringBuilder.append(" or ");
                }

                stringBuilder.append(RepositoryColumn.INSTANCE_GUID.getColumnName());
                stringBuilder.append(" = '");
                stringBuilder.append(escapePropertyValue(guid));
                stringBuilder.append("' ");
            }

            stringBuilder.append(") ");

            return stringBuilder.toString();
        }

        return " ";
    }


    /**
     * Return the where clause that identified the primary key of an entity, relationship or classification.
     *
     * @param instanceGUID unique identifier of the associated entity or relationship
     * @param version the version of the instance
     * @param classificationName optional classification name
     * @return where clause
     */
    public String getPrimaryKeysClause(String instanceGUID,
                                       long   version,
                                       String classificationName)
    {
        if ((instanceGUID != null) && (version != 0L))
        {
            String sqlFragment = "(" + RepositoryColumn.INSTANCE_GUID.getColumnName() + " = '" + instanceGUID + "' and " + RepositoryColumn.VERSION.getColumnName() + " = " + version;

            if (classificationName != null)
            {
                sqlFragment = sqlFragment + " and " + RepositoryColumn.CLASSIFICATION_NAME.getColumnName() +  " = '" + classificationName + "' ";
            }

            return sqlFragment + ")";
        }

        return  " ";
    }


    /**
     * Join the principle table with its associated attributes table.
     *
     * @param principleTableName name of main table
     * @param propertiesTableName name of attributes table
     * @param columnSelection name of a specific column to select
     * @return the join part of the SQL query
     */
    public String getPropertyJoinQuery(String principleTableName,
                                       String propertiesTableName,
                                       String columnSelection)
    {
        return "select " + columnSelection + " from " + principleTableName +
                    " left outer join " + propertiesTableName +
                    " on " + RepositoryColumn.INSTANCE_GUID.getColumnName(principleTableName) + " = " + RepositoryColumn.INSTANCE_GUID.getColumnName(propertiesTableName) +
                    " and " + RepositoryColumn.VERSION.getColumnName(principleTableName) + " = " + RepositoryColumn.VERSION.getColumnName(propertiesTableName);
    }


    /**
     * Join the principle table with its associated attributes table.
     *
     * @param principleTable main table
     * @param propertiesTableName name of attributes table
     * @return the join part of the SQL query
     */
    public String getDistinctPropertyJoinQuery(RepositoryTable principleTable,
                                               String          propertiesTableName)
    {
        StringBuilder clause = new StringBuilder("select distinct ");

        boolean firstColumn = true;

        for (String columnName : principleTable.getQualifiedColumnNames())
        {
            if (firstColumn)
            {
                firstColumn = false;
            }
            else
            {
                clause.append(", ");
            }

            clause.append(columnName);
        }

        clause.append(" from ");
        clause.append(principleTable.getTableName());
        clause.append(" left outer join ");
        clause.append(propertiesTableName);
        clause.append(" on ");
        clause.append(RepositoryColumn.INSTANCE_GUID.getColumnName(principleTable.getTableName()));
        clause.append(" = ");
        clause.append(RepositoryColumn.INSTANCE_GUID.getColumnName(propertiesTableName));
        clause.append(" and ");
        clause.append(RepositoryColumn.VERSION.getColumnName(principleTable.getTableName()));
        clause.append(" = ");
        clause.append(RepositoryColumn.VERSION.getColumnName(propertiesTableName));

        if (log.isDebugEnabled())
        {
            log.debug(this.toString());
            log.debug(clause.toString());
        }

        return clause.toString();
    }


    /**
     * Return the where clause built up from the query parameters supplied.
     *
     * @return SQL command fragment
     */
    public String getAsOfTimeWhereClause() throws RepositoryErrorException
    {
        String whereClause =
                getAsOfTimeClause() +
                getRelationshipEndGUIDClause() +
                getRelationshipEndCriteriaClause() +
                getGUIDListClause() +
                getSearchStringClause() +
                getSearchPropertiesClause(principleTableName, propertyTableName, searchProperties) +
                getSearchClassificationsClause() +
                getTypeClause() +
                getLimitResultsByClassificationClaus() +
                getLimitResultsByStatusClause();

        if (log.isDebugEnabled())
        {
            log.debug(this.toString());
            log.debug(whereClause);
        }

        return whereClause;
    }



    /**
     * The sequencing (order by) and paging (limit/offset) can only be added at the end and may only include
     *
     * @param principleTableName main table that the ordering will occur on
     * @return sql fragment
     */
    public String getSequenceAndPaging(String principleTableName)
    {
        String clause = getSequencingOrder(principleTableName) +
                        getPaging(principleTableName);

        if (log.isDebugEnabled())
        {
            log.debug(this.toString());
            log.debug(clause);
        }

        return clause + ";";
    }


    /**
     * toString() JSON-style
     *
     * @return string description
     */
    @Override
    public String toString()
    {
        return "QueryBuilder{" +
                "relationshipEndGUID='" + relationshipEndGUID + '\'' +
                ", searchString='" + searchString + '\'' +
                ", searchProperties=" + searchProperties +
                ", matchClassifications=" + matchClassifications +
                ", limitResultsByClassification=" + limitResultsByClassification +
                ", typeGUID='" + typeGUID + '\'' +
                ", typeGUIDParameterName='" + typeGUIDParameterName + '\'' +
                ", subtypeGUIDs=" + subtypeGUIDs +
                ", skipSubtypes=" + skipSubtypes +
                ", subTypeGUIDsParameterName='" + subTypeGUIDsParameterName + '\'' +
                ", limitResultsByStatus=" + limitResultsByStatus +
                ", asOfTime=" + asOfTime +
                ", sequencingProperty='" + sequencingProperty + '\'' +
                ", sequencingOrder=" + sequencingOrder +
                ", fromElement=" + fromElement +
                ", pageSize=" + pageSize +
                '}';
    }
}
