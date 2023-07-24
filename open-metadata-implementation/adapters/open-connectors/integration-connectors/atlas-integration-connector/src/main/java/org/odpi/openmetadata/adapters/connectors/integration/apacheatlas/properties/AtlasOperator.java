/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.apacheatlas.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.HashMap;
import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * AtlasOperator is used in the formulations of queries.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum AtlasOperator
{
    LT(new String[]{"<", "lt"}),
    GT(new String[]{">", "gt"}),
    LTE(new String[]{"<=", "lte"}),
    GTE(new String[]{">=", "gte"}),
    EQ(new String[]{"=", "eq"}),
    NEQ(new String[]{"!=", "neq"}),
    IN(new String[]{"in", "IN"}),
    LIKE(new String[]{"like", "LIKE"}),
    STARTS_WITH(new String[]{"startsWith", "STARTSWITH", "begins_with", "BEGINS_WITH"}),
    ENDS_WITH(new String[]{"endsWith", "ENDSWITH", "ends_with", "ENDS_WITH"}),
    CONTAINS(new String[]{"contains", "CONTAINS"}),
    NOT_CONTAINS(new String[]{"not_contains", "NOT_CONTAINS"}),
    CONTAINS_ANY(new String[]{"containsAny", "CONTAINSANY", "contains_any", "CONTAINS_ANY"}),
    CONTAINS_ALL(new String[]{"containsAll", "CONTAINSALL", "contains_all", "CONTAINS_ALL"}),
    IS_NULL(new String[]{"isNull", "ISNULL", "is_null", "IS_NULL"}),
    NOT_NULL(new String[]{"notNull", "NOTNULL", "not_null", "NOT_NULL"}),
    TIME_RANGE(new String[]{"timerange", "TIMERANGE", "time_range", "TIME_RANGE"}),
    NOT_EMPTY(new String[]{"notEmpty", "NOTEMPTY", "not_empty", "NOT_EMPTY"})
    ;

    static final Map<String, AtlasOperator> operatorsMap = new HashMap<>();

    private final String[] symbols;

    static  {
        for (AtlasOperator operator : AtlasOperator.values())
        {
            for (String s : operator.symbols)
            {
                operatorsMap.put(s, operator);
            }
        }
    }

    AtlasOperator(String[] symbols)
    {
        this.symbols = symbols;
    }

    public static AtlasOperator fromString(String symbol)
    {
        return operatorsMap.get(symbol);
    }

    public String getSymbol()
    {
        return symbols[0];
    }

    public String[] getSymbols()
    {
        return symbols;
    }

    @Override
    public String toString()
    {
        return getSymbol();
    }

}
