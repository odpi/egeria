/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.lineagewarehouse.model;


import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

/**
 * Specify a lineage query.
 */
public class NodeNamesSearchCriteria implements Serializable
{
    @Serial
    private static final long serialVersionUID = 1L;

    private String type;
    private String searchValue;
    private int limit;


    public NodeNamesSearchCriteria()
    {
    }


    public NodeNamesSearchCriteria(String type, String searchValue, int limit)
    {
        this.type        = type;
        this.searchValue = searchValue;
        this.limit       = limit;
    }


    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getSearchValue()
    {
        return searchValue;
    }

    public void setSearchValue(String searchValue)
    {
        this.searchValue = searchValue;
    }

    public int getLimit()
    {
        return limit;
    }

    public void setLimit(int limit)
    {
        this.limit = limit;
    }


    @Override
    public String toString()
    {
        return "NodeNamesSearchCriteria{" +
                "type='" + type + '\'' +
                ", searchValue='" + searchValue + '\'' +
                ", limit=" + limit +
                '}';
    }


    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare) return true;
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        NodeNamesSearchCriteria that = (NodeNamesSearchCriteria) objectToCompare;
        return limit == that.limit &&
                Objects.equals(type, that.type) &&
                Objects.equals(searchValue, that.searchValue);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(type, searchValue, limit);
    }
}
