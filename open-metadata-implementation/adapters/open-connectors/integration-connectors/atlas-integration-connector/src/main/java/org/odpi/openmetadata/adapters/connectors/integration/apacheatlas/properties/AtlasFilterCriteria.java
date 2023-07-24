/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.apacheatlas.properties;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * AtlasFilterCriteria can represent a single condition or group of conditions.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class AtlasFilterCriteria
{
    private String                    attributeName  = null;
    private AtlasOperator             operator       = null;
    private String                    attributeValue = null;
    private AtlasCondition            condition      = null;
    private List<AtlasFilterCriteria> criterion      = null;


    public AtlasFilterCriteria()
    {
    }


    public String getAttributeName()
    {
        return attributeName;
    }


    public void setAttributeName(String attributeName)
    {
        this.attributeName = attributeName;
    }


    public AtlasOperator getOperator()
    {
        return operator;
    }


    public void setOperator(AtlasOperator operator)
    {
        this.operator = operator;
    }


    public String getAttributeValue()
    {
        return attributeValue;
    }


    public void setAttributeValue(String attributeValue)
    {
        this.attributeValue = attributeValue;
    }


    public AtlasCondition getCondition()
    {
        return condition;
    }


    public void setCondition(AtlasCondition condition)
    {
        this.condition = condition;
    }


    public List<AtlasFilterCriteria> getCriterion()
    {
        return criterion;
    }


    public void setCriterion(List<AtlasFilterCriteria> criterion)
    {
        this.criterion = criterion;
    }


    @Override
    public String toString()
    {
        return "AtlasFilterCriteria{" +
                       "attributeName='" + attributeName + '\'' +
                       ", operator=" + operator +
                       ", attributeValue='" + attributeValue + '\'' +
                       ", condition=" + condition +
                       ", criterion=" + criterion +
                       '}';
    }
}
