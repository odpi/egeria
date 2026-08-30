/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.repositoryservices.postgres.repositoryconnector.database;

import org.odpi.openmetadata.adapters.repositoryservices.postgres.repositoryconnector.ffdc.PostgresErrorCode;
import org.odpi.openmetadata.adapters.connectors.resource.jdbc.ddl.postgres.PostgreSQLColumn;
import org.odpi.openmetadata.adapters.connectors.resource.jdbc.properties.ColumnType;
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
    private String                end1EntityTypeGUID           = null;
    private List<String>          end2EntityGUIDs              = null;
    private String                end2EntityTypeGUID           = null;
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
     * @param searchString desired value
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
     * Return a value in the form the target column needs.
     * <br>
     * Dates are the reason this exists, and the column matters.  A date is carried as epoch milliseconds:
     * that is exactly what the attribute table holds, since property_value is text, so an ordinary property
     * must be compared as the number it is stored as.  The header columns - effective_from_time and the
     * rest - are real timestamp columns, and comparing a bigint against one of those is rejected outright
     * ("date/time field value out of range") rather than simply not matching.
     *
     * @param propertyColumn the column being compared against
     * @param propertyValue the value supplied in the search condition
     * @return value ready to be placed in the SQL
     */
    private Object getSQLValue(String propertyColumn,
                               Object propertyValue)
    {
        if ((propertyValue instanceof Long dateAsLong) && (this.isDateColumn(propertyColumn)))
        {
            return new java.sql.Timestamp(dateAsLong).toString();
        }

        return propertyValue;
    }


    /**
     * Return whether the named column holds a timestamp.
     *
     * @param columnName column being compared against
     * @return true if the column's declared type is a date
     */
    private boolean isDateColumn(String columnName)
    {
        for (RepositoryColumn repositoryColumn : RepositoryColumn.values())
        {
            if (repositoryColumn.getColumnName().equals(columnName))
            {
                return repositoryColumn.getColumnType() == ColumnType.DATE;
            }
        }

        return false;
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

        propertyValue = this.getSQLValue(propertyColumn, propertyValue);

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
     * @param end1EntityTypeGUID optional unique identifier of the type that the entity at end 1 must
     *                           belong to.  Subtypes of the named type match too.  This is independent of
     *                           end1EntityGUIDs: supplying the type on its own, with the guids left null,
     *                           asks for the relationships that start at any entity of that type.
     * @param end2EntityGUIDs optional list of entity guids used to match end 2 of the relationships.
     * @param end2EntityTypeGUID optional unique identifier of the type that the entity at end 2 must
     *                           belong to.  Subtypes of the named type match too.  This is independent of
     *                           end2EntityGUIDs: supplying the type on its own, with the guids left null,
     *                           asks for the relationships that end at any entity of that type.
     * @param endMatchCriteria criteria for matching the ends of the relationships.
     */
    public void setRelationshipEndCriteria(List<String>     end1EntityGUIDs,
                                           String           end1EntityTypeGUID,
                                           List<String>     end2EntityGUIDs,
                                           String           end2EntityTypeGUID,
                                           EndMatchCriteria endMatchCriteria)
    {
        this.end1EntityGUIDs    = end1EntityGUIDs;
        this.end1EntityTypeGUID = end1EntityTypeGUID;
        this.end2EntityGUIDs    = end2EntityGUIDs;
        this.end2EntityTypeGUID = end2EntityTypeGUID;
        this.endMatchCriteria   = endMatchCriteria;
    }


    /**
     * Return the SQL that restricts one end of a relationship to entities of a particular type.
     * <br>
     * The relationship row records the guid at each end but not the type of the entity there, so the type
     * is tested by looking the entity up.  Subtypes are included, matching how a type is matched
     * everywhere else: the stored type name carries the whole hierarchy, so an entity of a subtype
     * contains the supertype's name.
     *
     * @param endColumnName the end guid column being constrained
     * @param endEntityTypeGUID the type the entity at that end must be
     * @param negate true when the caller asked for relationships that do NOT match
     * @return SQL fragment
     * @throws RepositoryErrorException the type is not known
     */
    private String getRelationshipEndTypeClause(String  endColumnName,
                                                String  endEntityTypeGUID,
                                                boolean negate) throws RepositoryErrorException
    {
        final String parameterName = "endEntityTypeGUID";

        String membershipOperand = negate ? " not in (" : " in (";

        return " (" + endColumnName + membershipOperand +
                       "select " + RepositoryColumn.INSTANCE_GUID.getColumnName(RepositoryTable.ENTITY.getTableName()) +
                       " from " + RepositoryTable.ENTITY.getTableName() +
                       " where (" + RepositoryColumn.VERSION_END_TIME.getColumnName() + " is null)" +
                       " and (" + RepositoryColumn.TYPE_NAME.getColumnName() + " like '%:" +
                       escapePropertyValue(getSafeLikePattern(this.lookUpTypeName(endEntityTypeGUID, parameterName))) +
                       ":%'))) ";
    }


    /**
     * Derive the SQL fragment that describes the criteria for one end of the relationship.  An end may be
     * constrained by the entities allowed there, by the type of entity allowed there, by both, or by
     * neither - and an unconstrained end matches anything, which is what lets a caller ask only about the
     * type at one end and say nothing at all about the other.
     * <br>
     * When the criteria are negated, the negation applies to the end as a whole: the relationships wanted
     * are the ones this end does <em>not</em> match, so the two parts are negated and joined with "or"
     * rather than negated and joined with "and".  Negating them separately would exclude the relationships
     * whose end is in the guid list <em>or</em> of that type, which is a different and larger set.
     *
     * @param endColumnName the column holding the entity guid for this end
     * @param endEntityGUIDs the entities allowed at this end, or null for any
     * @param endEntityTypeGUID the type allowed at this end, or null for any
     * @param negate is this end's criteria negated?
     * @return SQL fragment, or null if this end is not constrained
     * @throws RepositoryErrorException the type is not known to this repository
     */
    private String getRelationshipEndClause(String       endColumnName,
                                            List<String> endEntityGUIDs,
                                            String       endEntityTypeGUID,
                                            boolean      negate) throws RepositoryErrorException
    {
        String guidClause = null;

        if (endEntityGUIDs != null)
        {
            String matchComparison  = negate ? " != " : " = ";
            String guidMatchOperand = negate ? " and " : " or ";

            StringBuilder guidBuilder = new StringBuilder(" (");

            boolean firstGUID = true;

            for (String endEntityGUID : endEntityGUIDs)
            {
                if (firstGUID)
                {
                    firstGUID = false;
                }
                else
                {
                    guidBuilder.append(guidMatchOperand);
                }

                guidBuilder.append(endColumnName);
                guidBuilder.append(matchComparison);
                guidBuilder.append("'");
                guidBuilder.append(endEntityGUID);
                guidBuilder.append("'");
            }

            guidBuilder.append(") ");

            guidClause = guidBuilder.toString();
        }

        String typeClause = null;

        if (endEntityTypeGUID != null)
        {
            typeClause = this.getRelationshipEndTypeClause(endColumnName, endEntityTypeGUID, negate);
        }

        if (guidClause == null)
        {
            return typeClause;
        }

        if (typeClause == null)
        {
            return guidClause;
        }

        return " (" + guidClause + (negate ? " or " : " and ") + typeClause + ") ";
    }


    /**
     * Derive the SQL fragment to describe the relationship end criteria.
     *
     * @return SQL fragment, never null - a space when there are no criteria
     * @throws RepositoryErrorException a type named in the criteria is not known to this repository
     */
    private String getRelationshipEndCriteriaClause() throws RepositoryErrorException
    {
        if (endMatchCriteria == null)
        {
            return " ";
        }

        boolean negate = (endMatchCriteria == EndMatchCriteria.NONE);

        String end1Clause = this.getRelationshipEndClause(RepositoryColumn.END_1_GUID.getColumnName(),
                                                          end1EntityGUIDs,
                                                          end1EntityTypeGUID,
                                                          negate);

        String end2Clause = this.getRelationshipEndClause(RepositoryColumn.END_2_GUID.getColumnName(),
                                                          end2EntityGUIDs,
                                                          end2EntityTypeGUID,
                                                          negate);

        /*
         * ANY asks for the relationships that either end matches.  BOTH asks for the ones both ends match.
         * NONE asks for the ones neither end matches - and with each end already negated above, that is the
         * ends joined with "and".
         */
        String endMatchOperand = (endMatchCriteria == EndMatchCriteria.ANY) ? " or " : " and ";

        if ((end1Clause != null) && (end2Clause != null))
        {
            return " and (" + end1Clause + endMatchOperand + end2Clause + ") ";
        }

        if (end1Clause != null)
        {
            return " and (" + end1Clause + ") ";
        }

        if (end2Clause != null)
        {
            return " and (" + end2Clause + ") ";
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
            if (matchClassifications.getMatchCriteria() == MatchCriteria.ALL)
            {
                /*
                 * ALL is not expressible as one group of conditions over a single classification row: a row
                 * names one classification, so "type_name like X and type_name like Y" can never be true and
                 * the search returns nothing.  It is answered instead by one membership test per named
                 * classification - see getClassificationSubSelectWhereClauses().
                 */
                return " ";
            }

            StringBuilder stringBuilder  = new StringBuilder();
            StringBuilder conditionsBuilder = new StringBuilder();
            boolean       firstCondition  = true;

            /*
             * This clause selects the classification rows that name the classifications the caller asked
             * about.  It is always the positive set - "rows for classification X" - because it is used as a
             * sub-select of entity guids, and whether the caller wanted entities that carry those
             * classifications or entities that do not is decided by how that membership is applied.  See
             * isNegatedClassificationMatch().
             * <br>
             * Negating inside the sub-select does not express NONE.  "select the entities that have a
             * classification which is not X" returns an entity carrying both X and Y - it has Y - and misses
             * an entity carrying no classifications at all, which is the commonest case of not having X.
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
                    if (firstCondition)
                    {
                        conditionsBuilder.append(" (");
                        firstCondition = false;
                    }
                    else
                    {
                        conditionsBuilder.append(matchOperand);
                    }

                    conditionsBuilder.append(this.getSingleClassificationClause(classificationCondition));
                }
            }

            if (!firstCondition)
            {
                conditionsBuilder.append(") ");

                stringBuilder.append(" and ");
                stringBuilder.append(conditionsBuilder);
            }

            return stringBuilder.toString();
        }

        return " ";
    }


    /**
     * Return the SQL that selects the classification rows naming one classification.
     *
     * @param classificationCondition the classification asked about
     * @return SQL fragment
     * @throws RepositoryErrorException problem building the property conditions
     */
    private String getSingleClassificationClause(ClassificationCondition classificationCondition) throws RepositoryErrorException
    {
        StringBuilder conditionBuilder = new StringBuilder();

        conditionBuilder.append("(");
        conditionBuilder.append(RepositoryColumn.TYPE_NAME.getColumnName(RepositoryTable.CLASSIFICATION.getTableName()));
        conditionBuilder.append(" like '%:");

        /*
         * A condition with no name matches any classification, which keeps the SQL construction simple for a
         * caller that only wants to constrain the classification's properties.
         */
        if (classificationCondition.getName() != null)
        {
            conditionBuilder.append(classificationCondition.getName());
        }
        else
        {
            conditionBuilder.append("%");
        }
        conditionBuilder.append(":%' ");

        if (classificationCondition.getMatchProperties() != null)
        {
            conditionBuilder.append(this.getSearchPropertiesClause(RepositoryTable.CLASSIFICATION.getTableName(),
                                                                    RepositoryTable.CLASSIFICATION_ATTRIBUTE_VALUE.getTableName(),
                                                                    classificationCondition.getMatchProperties()));
        }

        conditionBuilder.append(") ");

        return conditionBuilder.toString();
    }


    /**
     * Return the where clause for each sub-select needed to express the classification search.
     * <br>
     * ANY and NONE need one sub-select: the entities carrying any of the named classifications, either
     * included or excluded.  ALL needs one per classification, because an entity carries each of them on a
     * separate row and a single row cannot satisfy two names at once - which is why asking for two
     * classifications used to return nothing at all.
     *
     * @return one where clause per sub-select; empty if there is no classification search
     * @throws RepositoryErrorException problem building the conditions
     */
    public List<String> getClassificationSubSelectWhereClauses() throws RepositoryErrorException
    {
        List<String> whereClauses = new ArrayList<>();

        if ((matchClassifications == null) || (matchClassifications.getConditions() == null))
        {
            /*
             * No classification search - but this builder may still be carrying a
             * limitResultsByClassification constraint, which lives in the same where clause and needs the
             * same sub-select.  Returning nothing here drops that constraint silently, and a search that
             * has quietly stopped filtering returns more than it should rather than failing.
             */
            whereClauses.add(this.getAsOfTimeWhereClause());

            return whereClauses;
        }

        if (matchClassifications.getMatchCriteria() == MatchCriteria.ALL)
        {
            for (ClassificationCondition classificationCondition : matchClassifications.getConditions())
            {
                if (classificationCondition != null)
                {
                    /*
                     * getAsOfTimeWhereClause() rather than just the asOfTime clause: it carries everything
                     * else the sub-select needs - the status limit above all, without which a classification
                     * that has been removed is still matched, since removing one soft-deletes the row rather
                     * than ending its version.  For ALL it contributes no classification condition of its
                     * own (see getSearchClassificationsClause()), so the single condition is added here.
                     */
                    whereClauses.add(this.getAsOfTimeWhereClause() + " and " + this.getSingleClassificationClause(classificationCondition));
                }
            }
        }
        else
        {
            whereClauses.add(this.getAsOfTimeWhereClause());
        }

        return whereClauses;
    }


    /**
     * Return the complete SQL that restricts a query to the entities the classification search asks for.
     * <br>
     * The whole membership expression is built here rather than handing callers the pieces to assemble.
     * The pieces are easy to assemble wrongly - each sub-select needs the base clauses as well as its own
     * classification condition, ALL needs one membership test per classification while ANY and NONE need
     * one in total, and NONE inverts the membership instead of the condition.  Getting any of those wrong
     * does not fail: the query simply stops filtering, or filters on the wrong thing, and returns a
     * plausible answer.
     *
     * @param entityGUIDColumn the qualified entity guid column the membership applies to
     * @return SQL fragment, beginning with "and", or empty if there is no classification search
     * @throws RepositoryErrorException problem building the conditions
     */
    public String getClassificationMembershipClause(String entityGUIDColumn) throws RepositoryErrorException
    {
        StringBuilder membershipBuilder = new StringBuilder();

        String membershipOperand = this.isNegatedClassificationMatch() ? " not in (" : " in (";

        for (String subSelectWhereClause : this.getClassificationSubSelectWhereClauses())
        {
            membershipBuilder.append(" and ")
                             .append(entityGUIDColumn)
                             .append(membershipOperand)
                             .append("select ")
                             .append(RepositoryColumn.INSTANCE_GUID.getColumnName(RepositoryTable.CLASSIFICATION.getTableName()))
                             .append(" from ")
                             .append(RepositoryTable.CLASSIFICATION.getTableName())
                             .append(" where ")
                             .append(subSelectWhereClause)
                             .append(")");
        }

        return membershipBuilder.toString();
    }


    /**
     * Return whether the caller asked for entities that do NOT carry the classifications named in the
     * search.  The sub-select this builder produces always selects the entities that DO carry them, so a
     * NONE request is served by excluding that set rather than by selecting a different one.
     *
     * @return true if the classification membership should be negated
     */
    public boolean isNegatedClassificationMatch()
    {
        return (matchClassifications != null) && (matchClassifications.getMatchCriteria() == MatchCriteria.NONE);
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
     * Return the ORDER BY fragment.
     * <br><br>
     * Whatever the caller asked to sort by, the fragment always ends with the principle table's primary key
     * (see getUniqueOrderClause()).  Sorting by the requested column alone is not enough once the query is
     * paged: none of the columns a caller can sequence on - creation time, update time, a property value -
     * is unique, and each page is a separate execution of "order by ... limit ... offset ...", not a
     * server-side cursor.  Rows that tie on the sort column may therefore be ordered differently by each
     * execution, so an element can move between offset windows from one page fetch to the next and be
     * returned twice, or skipped entirely, while the traversal still terminates normally.  Appending the
     * primary key makes the ordering a deterministic total order, which is what makes the offsets line up.
     * <br><br>
     * This matters most for the broad searches - a whole base type such as Referenceable - where thousands
     * of elements bulk-loaded from an archive share the same creation timestamp, and for a
     * sequencingProperty that is null for an entire type (every element ties at the null position).
     *
     * @param principleTableName main table that the ordering will occur on
     * @return sequencing
     */
    private String getSequencingOrder(String principleTableName)
    {
        if (sequencingOrder == null)
        {
            return " ";
        }

        String sortExpression = null;
        String sortDirection  = "asc";

        switch (sequencingOrder)
        {
            case ANY, CREATION_DATE_RECENT ->
            {
                sortExpression = RepositoryColumn.CREATE_TIME.getColumnName(principleTableName);
                sortDirection  = "desc";
            }
            case CREATION_DATE_OLDEST ->
            {
                sortExpression = RepositoryColumn.CREATE_TIME.getColumnName(principleTableName);
            }
            case LAST_UPDATE_RECENT ->
            {
                sortExpression = RepositoryColumn.UPDATE_TIME.getColumnName(principleTableName);
                sortDirection  = "desc";
            }
            case LAST_UPDATE_OLDEST ->
            {
                sortExpression = RepositoryColumn.UPDATE_TIME.getColumnName(principleTableName);
            }
            case GUID ->
            {
                /*
                 * The primary key begins with instance_guid, so the tie-breaker below already is exactly
                 * the ordering being asked for - and is a total order on its own.
                 */
            }
            case PROPERTY_DESCENDING ->
            {
                sortExpression = this.getSequencingPropertyOrderClause(principleTableName);
                sortDirection  = "desc";
            }
            case PROPERTY_ASCENDING ->
            {
                sortExpression = this.getSequencingPropertyOrderClause(principleTableName);
            }
        }

        StringBuilder orderByClause = new StringBuilder(" order by ");

        if (sortExpression != null)
        {
            orderByClause.append(sortExpression).append(" ").append(sortDirection).append(", ");
        }

        orderByClause.append(this.getUniqueOrderClause(principleTableName)).append(" ");

        return orderByClause.toString();
    }


    /**
     * Return the SQL fragment that orders by the principle table's primary key, which is the value that
     * turns any of the orderings above into a deterministic total order.  The primary key is used rather
     * than instance_guid alone because it is the definition of a unique row for the table being paged:
     * (instance_guid, version) for the entity and relationship tables, and (instance_guid,
     * classification_name, version) for the classification table, where one instance_guid legitimately has
     * a row per classification.
     * <br><br>
     * The direction is always ascending.  Which way the tie-break runs makes no difference to whether the
     * paging is correct - only that every execution of the query resolves the tie the same way.
     *
     * @param principleTableName main table that the ordering will occur on
     * @return SQL fragment listing the sort columns - does not include "order by"
     */
    private String getUniqueOrderClause(String principleTableName)
    {
        StringBuilder uniqueOrderClause = new StringBuilder();

        for (RepositoryTable repositoryTable : RepositoryTable.values())
        {
            if (repositoryTable.getTableName().equals(principleTableName))
            {
                boolean firstColumn = true;

                for (PostgreSQLColumn primaryKeyColumn : repositoryTable.getPrimaryKeys())
                {
                    if (firstColumn)
                    {
                        firstColumn = false;
                    }
                    else
                    {
                        uniqueOrderClause.append(", ");
                    }

                    uniqueOrderClause.append(principleTableName).append(".").append(primaryKeyColumn.getColumnName()).append(" asc");
                }

                break;
            }
        }

        if (uniqueOrderClause.isEmpty())
        {
            /*
             * An unrecognized table - every table this class queries has instance_guid, so it is the safest
             * value to fall back to, and an incomplete tie-break is still better than none.
             */
            uniqueOrderClause.append(RepositoryColumn.INSTANCE_GUID.getColumnName(principleTableName)).append(" asc");
        }

        return uniqueOrderClause.toString();
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
