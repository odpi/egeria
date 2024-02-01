/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.assetcatalog.beans;


import java.util.Objects;

public class NodeNamesSearchCriteria
{
    private String type;
    private String searchValue;
    private int limit;


    public NodeNamesSearchCriteria()
    {
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
        if (this == objectToCompare)
        {
            return true;
        }
        if (! (objectToCompare instanceof NodeNamesSearchCriteria that))
        {
            return false;
        }
        return limit == that.limit && Objects.equals(type, that.type) && Objects.equals(searchValue, that.searchValue);
    }


    @Override
    public int hashCode()
    {
        return Objects.hash(type, searchValue, limit);
    }
}
