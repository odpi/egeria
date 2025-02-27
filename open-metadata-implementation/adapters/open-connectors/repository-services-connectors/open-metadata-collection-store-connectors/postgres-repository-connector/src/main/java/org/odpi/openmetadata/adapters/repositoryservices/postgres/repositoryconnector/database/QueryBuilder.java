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
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.PrimitiveDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
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
    private String                searchString                 = null;
    private SearchProperties      searchProperties             = null;
    private String                principleTableName           = null;
    private String                propertyTableName            = null;
    private SearchClassifications matchClassifications         = null;
    private List<String>          limitResultsByClassification = null;
    private String                typeGUID                     = null;
    private String                typeGUIDParameterName        = "typeGUID";
    private List<String>          subtypeGUIDs                 = null;
    private String                subTypeGUIDsParameterName    = "subTypeGUIDs";
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
            return " and (" + RepositoryColumn.END_1_GUID.getColumnName() + " = '" + relationshipEndGUID +
                    "' or " + RepositoryColumn.END_2_GUID.getColumnName() + " = '" + relationshipEndGUID + "')";
        }

        return " ";
    }

    /**
     * Set up the Java regular expression used to match against any of the String property values
     * within instances of the specified type(s).
     *
     * @param searchString regex
     */
    public void setSearchString(String searchString)
    {
        this.searchString = searchString;
    }


    /**
     * Return the SQL search string that needs to appear in the SQL query.
     *
     * @return fragment of SQL
     * @throws RepositoryErrorException problem with the regEx
     */
    private String getSearchStringClause() throws RepositoryErrorException
    {
        if (searchString != null)
        {
            if (repositoryHelper.isCaseInsensitiveRegex(searchString))
            {
                return " and " + getPropertySubSelect(null,
                                                      null,
                                                      " ~* ",
                                                      this.getSafeRegex(searchString));
            }
            else
            {
                return " and " + getPropertySubSelect(null,
                                                      null,
                                                      " ~ ",
                                                      this.getSafeRegex(searchString));
            }
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
     * @return sub select statement
     */
    private String getPropertySubSelect(String propertyName,
                                        String propertyColumn,
                                        String operator,
                                        String propertyValue)
    {
        String subSelect  = " (" + RepositoryColumn.INSTANCE_GUID.getColumnName(principleTableName) +
                                   " in (select " + RepositoryColumn.INSTANCE_GUID.getColumnName(propertyTableName) + " from " + propertyTableName +
                                          " where (";

        if (propertyName != null)
        {
            subSelect = subSelect + propertyColumn + "='" + propertyName + "'";

            if (propertyValue != null)
            {
                subSelect = subSelect + " and ";
            }
        }

        if (propertyValue != null)
        {
            subSelect = subSelect + RepositoryColumn.PROPERTY_VALUE.getColumnName() + " " + operator + "  '" + propertyValue + "'";
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
     * Check that a search string, which might contain a regEx expression, is safe to execute.
     * Dangerous RegEx can be used in a denial of service attack.  This simple time test ensures that the regEx is safe.
     *
     * @param suppliedSearchString the string to escape to avoid being interpreted as a regular expression
     * @return string  that is a safe regular expression
     * @throws RepositoryErrorException the RegEx takes to long to execute
     */
    @SuppressWarnings(value="all")
    private String getSafeRegex(Object suppliedSearchString) throws RepositoryErrorException
    {
        final String methodName = "getSafeRegex";
        final String tester     = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";

        if (suppliedSearchString != null)
        {
            String strippedSearchString = repositoryHelper.getUnqualifiedLiteralString(suppliedSearchString.toString());

            /*
             * Do not care about the result - just the time it takes
             */
            Date currentTime      = new Date();
            Date maxExecutionTime = new Date(currentTime.getTime() + 500);
            try
            {
                boolean matchString = tester.matches(strippedSearchString);
            }
            catch (Exception badRegex)
            {
                // This is not a problem as we are not necessarily expecting it to be a regex.
                // If it is supposed to be a regex, and it is ill-formed, PostgreSQL will throw it out.
                // This test is just to catch valid RegEx's that take a long time to execute.
            }

            Date completionTime = new Date();

            if (completionTime.after(maxExecutionTime))
            {
                throw new RepositoryErrorException(PostgresErrorCode.BAD_REGEX.getMessageDefinition(searchString),
                                                    this.getClass().getName(),
                                                    methodName);
            }

            return strippedSearchString;
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
     * @return sql fragment wrapped in parentheses.  Forms part of a where clause
     */
    private String getPropertyComparisonFromInstanceProperties(InstanceProperties         instanceProperties,
                                                               String                     topLevelPropertyName,
                                                               PropertyComparisonOperator stringPropertyOperator,
                                                               PropertyComparisonOperator numericPropertyOperator,
                                                               String                     matchOperand) throws RepositoryErrorException
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
                                                                                    this.escapePropertyValue(primitivePropertyValue.getPrimitiveValue())));
                    }
                    else
                    {
                        stringBuilder.append(this.getNestedPropertyComparisonClause(topLevelPropertyName,
                                                                                    leafPropertyName,
                                                                                    numericPropertyOperator,
                                                                                    primitivePropertyValue.getPrimitiveValue()));
                    }
                }
                else if (instancePropertyValue instanceof EnumPropertyValue enumPropertyValue)
                {
                    stringBuilder.append(this.getNestedPropertyComparisonClause(topLevelPropertyName,
                                                                                leafPropertyName,
                                                                                stringPropertyOperator,
                                                                                this.escapePropertyValue(enumPropertyValue.getSymbolicName())));
                }
                else if (instancePropertyValue instanceof MapPropertyValue mapPropertyValue)
                {
                    stringBuilder.append(getPropertyComparisonFromInstanceProperties(mapPropertyValue.getMapValues(),
                                                                                     leafPropertyName,
                                                                                     stringPropertyOperator,
                                                                                     numericPropertyOperator,
                                                                                     matchOperand));
                }
                else if (instancePropertyValue instanceof ArrayPropertyValue arrayPropertyValue)
                {
                    stringBuilder.append(getPropertyComparisonFromInstanceProperties(arrayPropertyValue.getArrayValues(),
                                                                                     leafPropertyName,
                                                                                     stringPropertyOperator,
                                                                                     numericPropertyOperator,
                                                                                     matchOperand));
                }
                else if (instancePropertyValue instanceof StructPropertyValue structPropertyValue)
                {
                    stringBuilder.append(getPropertyComparisonFromInstanceProperties(structPropertyValue.getAttributes(),
                                                                                     leafPropertyName,
                                                                                     stringPropertyOperator,
                                                                                     numericPropertyOperator,
                                                                                     matchOperand));
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
     * @return sql fragment
     * @throws RepositoryErrorException the property does not make sense with the operator
     */
    private String getNestedPropertyComparisonClause(String                     topLevelPropertyName,
                                                     String                     leafPropertyName,
                                                     PropertyComparisonOperator operator,
                                                     Object                     propertyValue) throws RepositoryErrorException
    {
        final String methodName = "getNestedPropertyComparisonClause";


        String propertyColumn = this.mapPropertyNameToColumn(leafPropertyName, RepositoryColumn.ATTRIBUTE_NAME.getColumnName());

        if (propertyColumn.equals(RepositoryColumn.ATTRIBUTE_NAME.getColumnName()) || propertyColumn.equals(RepositoryColumn.PROPERTY_NAME.getColumnName()))
        {
            if (operator == PropertyComparisonOperator.IS_NULL)
            {
                if (propertyTableName != null)
                {
                    String subSelect = " (" + RepositoryColumn.INSTANCE_GUID.getColumnName(principleTableName) +
                            " not in (select " + RepositoryColumn.INSTANCE_GUID.getColumnName(propertyTableName) + " from " + propertyTableName +
                            " where (";
                    if (topLevelPropertyName == null)
                    {
                        return subSelect + RepositoryColumn.ATTRIBUTE_NAME.getColumnName() + " = '" + leafPropertyName + "'))) ";
                    }
                    else
                    {
                        return subSelect + RepositoryColumn.ATTRIBUTE_NAME.getColumnName() + " = '" + topLevelPropertyName + "' and " + RepositoryColumn.PROPERTY_NAME.getColumnName() + " like '%:" + leafPropertyName + "'))) ";
                    }
                }
            }
            else
            {
                String sqlClause = " (" + RepositoryColumn.INSTANCE_GUID.getColumnName(principleTableName) +
                        " in (select " + RepositoryColumn.INSTANCE_GUID.getColumnName(propertyTableName) + " from " + propertyTableName +
                        " where (";;

                if (topLevelPropertyName == null)
                {
                    sqlClause = sqlClause + RepositoryColumn.ATTRIBUTE_NAME.getColumnName() + " = '" + leafPropertyName + "'";
                }
                else
                {
                    sqlClause = sqlClause + RepositoryColumn.ATTRIBUTE_NAME.getColumnName() + " = '" + topLevelPropertyName + "' and ";
                    sqlClause = sqlClause + RepositoryColumn.PROPERTY_NAME.getColumnName() + " like '%:" + leafPropertyName + "'";
                }
                switch (operator)
                {
                    case EQ ->
                    {
                        return sqlClause + " and " + RepositoryColumn.PROPERTY_VALUE.getColumnName() + " = '" + this.getSafeRegex(propertyValue) + "'))) ";
                    }
                    case NEQ ->
                    {
                        return sqlClause + " and " + RepositoryColumn.PROPERTY_VALUE.getColumnName() + " != '" + this.getSafeRegex(propertyValue) + "'))) ";
                    }
                    case LT ->
                    {
                        return sqlClause + " and " + RepositoryColumn.PROPERTY_VALUE.getColumnName() + " < '" + propertyValue + "'))) ";
                    }
                    case LTE ->
                    {
                        return sqlClause + " and " + RepositoryColumn.PROPERTY_VALUE.getColumnName() + " <= '" + propertyValue + "'))) ";
                    }
                    case GT ->
                    {
                        return sqlClause + " and " + RepositoryColumn.PROPERTY_VALUE.getColumnName() + " > '" + propertyValue + "'))) ";
                    }
                    case GTE ->
                    {
                        return sqlClause + " and " + RepositoryColumn.PROPERTY_VALUE.getColumnName() + " >= '" + propertyValue + "'))) ";
                    }
                    case LIKE ->
                    {
                        if (repositoryHelper.isCaseInsensitiveRegex(propertyValue.toString()))
                        {
                            return sqlClause + " and " + RepositoryColumn.PROPERTY_VALUE.getColumnName() + " ~* '" + this.getSafeRegex(propertyValue) + "'))) ";
                        }
                        else
                        {
                            return sqlClause + " and " + RepositoryColumn.PROPERTY_VALUE.getColumnName() + " ~ '" + this.getSafeRegex(propertyValue) + "'))) ";
                        }
                    }
                    case NOT_LIKE ->
                    {
                        if (repositoryHelper.isCaseInsensitiveRegex(propertyValue.toString()))
                        {
                            return sqlClause + " and " + RepositoryColumn.PROPERTY_VALUE.getColumnName() + " !~* '" + this.getSafeRegex(propertyValue) + "'))) ";
                        }
                        else
                        {
                            return sqlClause + " and " + RepositoryColumn.PROPERTY_VALUE.getColumnName() + " !~ '" + this.getSafeRegex(propertyValue) + "'))) ";
                        }
                    }
                    case NOT_NULL ->
                    {
                        return sqlClause + ")))";
                    }
                }
            }
        }
        else // property in dedicated column
        {
            switch (operator)
            {
                case EQ ->
                {
                    return " (" + principleTableName + "." + propertyColumn + " = '" + this.getSafeRegex(propertyValue) + "') ";
                }
                case NEQ ->
                {
                    return " (" + principleTableName + "." + propertyColumn + " != '" + this.getSafeRegex(propertyValue) + "') ";
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
                    return " (" + principleTableName + "." + propertyColumn + " is null)";
                }
                case NOT_NULL ->
                {
                    return " (" + principleTableName + "." + propertyColumn + " is not null)";
                }
                case LIKE ->
                {
                    if (repositoryHelper.isCaseInsensitiveRegex(propertyValue.toString()))
                    {
                        return " (" + principleTableName + "." + propertyColumn + " ~* '" + this.getSafeRegex(propertyValue) + "') ";
                    }
                    else
                    {
                        return " (" + principleTableName + "." + propertyColumn + " ~ '" + this.getSafeRegex(propertyValue) + "') ";
                    }
                }
                case NOT_LIKE ->
                {
                    if (repositoryHelper.isCaseInsensitiveRegex(propertyValue.toString()))
                    {
                        return " (" + principleTableName + "." + propertyColumn + " !~* '" + this.getSafeRegex(propertyValue) + "') ";
                    }
                    else
                    {
                        return " (" + principleTableName + "." + propertyColumn + " !~ '" + this.getSafeRegex(propertyValue) + "') ";
                    }
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
     *
     * @return fragment of SQL
     */
    private String getSearchPropertiesClause() throws RepositoryErrorException
    {
        if (searchProperties != null)
        {
            return " and " + this.getPropertyComparisonFromPropertyConditions(searchProperties,
                                                                              propertyTableName,
                                                                              null);
        }

        return " ";
    }


    /**
     * Step through the hierarchy of properties, building out the nested clauses of the search query.
     *
     * @param searchProperties collection of properties to work on (most will be primitives)
     * @param propertyTableName name of table holding the properties
     * @param topLevelPropertyName parent attribute name - not null when dealing with nested properties
     * @return sql fragment wrapped in parentheses.  Forms part of a where clause
     */
    private String getPropertyComparisonFromPropertyConditions(SearchProperties searchProperties,
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

            StringBuilder stringBuilder = new StringBuilder("(");
            boolean       firstProperty = true;

            for (PropertyCondition propertyCondition : searchProperties.getConditions())
            {
                if (propertyCondition.getNestedConditions() != null)
                {
                    if (firstProperty)
                    {
                        firstProperty = false;
                    }
                    else
                    {
                        stringBuilder.append(matchOperand);
                    }

                    stringBuilder.append(this.getPropertyComparisonFromPropertyConditions(propertyCondition.getNestedConditions(),
                                                                                          propertyTableName,
                                                                                          topLevelPropertyName));
                }
                else
                {
                    String leafPropertyName = propertyCondition.getProperty();

                    InstancePropertyValue instancePropertyValue = propertyCondition.getValue();

                    if (firstProperty)
                    {
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
                                                                                        this.escapePropertyValue(primitivePropertyValue.getPrimitiveValue())));
                        }
                        else
                        {
                            stringBuilder.append(this.getNestedPropertyComparisonClause(topLevelPropertyName,
                                                                                        leafPropertyName,
                                                                                        propertyCondition.getOperator(),
                                                                                        primitivePropertyValue.getPrimitiveValue()));
                        }
                    }
                    else if (instancePropertyValue instanceof EnumPropertyValue enumPropertyValue)
                    {
                        stringBuilder.append(this.getNestedPropertyComparisonClause(topLevelPropertyName,
                                                                                    leafPropertyName,
                                                                                    propertyCondition.getOperator(),
                                                                                    this.escapePropertyValue(enumPropertyValue.getSymbolicName())));
                    }
                    else if (instancePropertyValue instanceof MapPropertyValue mapPropertyValue)
                    {
                        stringBuilder.append(getPropertyComparisonFromInstanceProperties(mapPropertyValue.getMapValues(),
                                                                                         leafPropertyName,
                                                                                         propertyCondition.getOperator(),
                                                                                         propertyCondition.getOperator(),
                                                                                         matchOperand));
                    }
                    else if (instancePropertyValue instanceof ArrayPropertyValue arrayPropertyValue)
                    {
                        stringBuilder.append(getPropertyComparisonFromInstanceProperties(arrayPropertyValue.getArrayValues(),
                                                                                         leafPropertyName,
                                                                                         propertyCondition.getOperator(),
                                                                                         propertyCondition.getOperator(),
                                                                                         matchOperand));
                    }
                    else if (instancePropertyValue instanceof StructPropertyValue structPropertyValue)
                    {
                        stringBuilder.append(getPropertyComparisonFromInstanceProperties(structPropertyValue.getAttributes(),
                                                                                         leafPropertyName,
                                                                                         propertyCondition.getOperator(),
                                                                                         propertyCondition.getOperator(),
                                                                                         matchOperand));
                    }
                    else // null property value
                    {
                        stringBuilder.append(this.getNestedPropertyComparisonClause(topLevelPropertyName,
                                                                                    leafPropertyName,
                                                                                    propertyCondition.getOperator(),
                                                                                    null));
                    }
                }
            }

            stringBuilder.append(") ");

            return stringBuilder.toString();
        }

        return " ";
    }


    /**
     * Set up optional list of entity classifications to match.
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
            String matchOperand = " and ";
            PropertyComparisonOperator operator = PropertyComparisonOperator.EQ;

            if (matchClassifications.getMatchCriteria() == MatchCriteria.ANY)
            {
                matchOperand = " or ";
            }
            else if (matchClassifications.getMatchCriteria() == MatchCriteria.NONE)
            {
                operator = PropertyComparisonOperator.NEQ;
            }

            StringBuilder stringBuilder  = new StringBuilder();

            for (ClassificationCondition classificationCondition : matchClassifications.getConditions())
            {
                if (classificationCondition != null)
                {
                    stringBuilder.append(matchOperand);
                    stringBuilder.append(" (");
                    stringBuilder.append(RepositoryColumn.CLASSIFICATION_NAME.getColumnName(RepositoryTable.CLASSIFICATION.getTableName()));
                    if (operator == PropertyComparisonOperator.EQ)
                    {
                        stringBuilder.append(" = '");
                    }
                    else
                    {
                        stringBuilder.append(" != '");
                    }
                    stringBuilder.append(classificationCondition.getName());
                    stringBuilder.append("' ");

                    if (classificationCondition.getMatchProperties() != null)
                    {
                        stringBuilder.append(" and ");
                        stringBuilder.append(this.getPropertyComparisonFromPropertyConditions(classificationCondition.getMatchProperties(),
                                                                                              RepositoryTable.CLASSIFICATION_ATTRIBUTE_VALUE.getTableName(),
                                                                                              null));
                    }

                    stringBuilder.append(") ");
                }
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
                stringBuilder.append(RepositoryColumn.CLASSIFICATION_NAME.getColumnName(RepositoryTable.CLASSIFICATION.getTableName()));
                stringBuilder.append(" = '");
                stringBuilder.append(classificationName);
                stringBuilder.append("'");
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
        this.typeGUID = typeGUID;
        this.typeGUIDParameterName = typeGUIDParameterName;
        this.subtypeGUIDs = subTypeGUIDs;
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
            StringBuilder stringBuffer = new StringBuilder(" and (");
            boolean       firstType    = true;

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
                stringBuffer.append(":%')");
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
            return " (" + RepositoryColumn.VERSION_START_TIME.getColumnName() + " < '" + asOfTime + "' and (" + RepositoryColumn.VERSION_END_TIME.getColumnName() + " is null or " + RepositoryColumn.VERSION_END_TIME.getColumnName() + " > '" + asOfTime + "')) ";
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
                    // todo temporary restriction
                    return " order by " + RepositoryColumn.CREATE_TIME.getColumnName(principleTableName) + " desc ";
                    // return " order by " + this.mapPropertyNameToColumn(sequencingProperty, RepositoryColumn.ATTRIBUTE_NAME.getColumnName()) + " desc ";
                }
                case PROPERTY_ASCENDING ->
                {
                    // todo temporary restriction
                    return " order by " + RepositoryColumn.CREATE_TIME.getColumnName(principleTableName) + " asc ";
                    // return " order by " + this.mapPropertyNameToColumn(sequencingProperty, RepositoryColumn.ATTRIBUTE_NAME.getColumnName()) + " asc ";
                }
            }
        }

        return " ";
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
        else if (OpenMetadataProperty.CREATED_BY.name.equals(propertyName))
        {
            return RepositoryColumn.CREATED_BY.getColumnName();
        }
        else if (OpenMetadataProperty.UPDATED_BY.name.equals(propertyName))
        {
            return RepositoryColumn.UPDATED_BY.getColumnName();
        }
        else if (OpenMetadataProperty.CURRENT_STATUS.name.equals(propertyName))
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
     * @param pageSize maximum number of elements that can be returned
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
                stringBuilder.append(guid);
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
                getGUIDListClause() +
                getSearchStringClause() +
                getSearchPropertiesClause() +
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
