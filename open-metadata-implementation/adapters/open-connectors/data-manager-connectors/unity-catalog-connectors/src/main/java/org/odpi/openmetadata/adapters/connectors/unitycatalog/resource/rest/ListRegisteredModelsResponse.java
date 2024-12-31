/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.unitycatalog.resource.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.properties.RegisteredModelInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * Return a list of models
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ListRegisteredModelsResponse
{
    private List<RegisteredModelInfo> registered_models = new ArrayList<>();
    private String                    next_page_token   = null;


    /**
     * Constructor
     */
    public ListRegisteredModelsResponse()
    {
    }


    /**
     * Return the list of registered models.
     *
     * @return list
     */
    public List<RegisteredModelInfo> getRegistered_models()
    {
        return registered_models;
    }


    /**
     * Return the list of registered models.
     *
     * @param registered_models list
     */
    public void setRegistered_models(List<RegisteredModelInfo> registered_models)
    {
        this.registered_models = registered_models;
    }


    /**
     * Return the opaque token to retrieve the next page of results. Absent if there are no more pages. page_token should be set to this value for the next request (for the next page of results).
     *
     * @return token
     */
    public String getNext_page_token()
    {
        return next_page_token;
    }


    /**
     * Set up the opaque token to retrieve the next page of results. Absent if there are no more pages. page_token should be set to this value for the next request (for the next page of results).
     *
     * @param next_page_token token
     */
    public void setNext_page_token(String next_page_token)
    {
        this.next_page_token = next_page_token;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "ListRegisteredResponse{" +
                "registered models=" + registered_models +
                ", nextPageToken='" + next_page_token + '\'' +
                '}';
    }


    /**
     * Compare the values of the supplied object with those stored in the current object.
     *
     * @param objectToCompare supplied object
     * @return boolean result of comparison
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare) return true;
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        ListRegisteredModelsResponse that = (ListRegisteredModelsResponse) objectToCompare;
        return Objects.equals(registered_models, that.registered_models) && Objects.equals(next_page_token, that.next_page_token);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(registered_models, next_page_token);
    }
}
