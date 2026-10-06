/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.wso2mi.resource.rest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.adapters.connectors.wso2mi.properties.APIInfo;

import java.util.List;

/**
 * ListAPIsResponse maps the payload returned by the WSO2 Micro Integrator Management API's
 * {@code GET /management/apis} resource:
 *
 * <pre>
 * { "count": 2, "list": [ { "name": "helloApi", "url": "http://host:8290/api" }, ... ] }
 * </pre>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ListAPIsResponse
{
    private int           count = 0;
    private List<APIInfo> list  = null;


    /**
     * Default constructor.
     */
    public ListAPIsResponse()
    {
    }


    /**
     * Return the number of deployed APIs reported by the Micro Integrator.
     *
     * @return int count
     */
    public int getCount()
    {
        return count;
    }


    /**
     * Set up the number of deployed APIs.
     *
     * @param count int count
     */
    public void setCount(int count)
    {
        this.count = count;
    }


    /**
     * Return the list of deployed APIs.
     *
     * @return list of {@link APIInfo}
     */
    public List<APIInfo> getList()
    {
        return list;
    }


    /**
     * Set up the list of deployed APIs.
     *
     * @param list list of {@link APIInfo}
     */
    public void setList(List<APIInfo> list)
    {
        this.list = list;
    }


    /**
     * JSON-style toString.
     *
     * @return string with the property names and values
     */
    @Override
    public String toString()
    {
        return "ListAPIsResponse{" +
                       "count=" + count +
                       ", list=" + list +
                       '}';
    }
}
