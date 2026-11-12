/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.dcat.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * DcatDataService describes a collection of operations that provides access to one or more datasets (dcat:DataService).
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class DcatDataService extends DcatResource
{
    private List<String> endpointURLs = null;
    private List<String> endpointDescriptions = null;
    private List<String> servesDatasets = null;


    /**
     * Default constructor
     */
    public DcatDataService()
    {
    }


    /**
     * Return the root locations of the service (dcat:endpointURL).
     *
     * @return list of URLs
     */
    public List<String> getEndpointURLs()
    {
        return endpointURLs;
    }


    /**
     * Set up the root locations of the service (dcat:endpointURL).
     *
     * @param endpointURLs list of URLs
     */
    public void setEndpointURLs(List<String> endpointURLs)
    {
        this.endpointURLs = endpointURLs;
    }


    /**
     * Return the descriptions of the service's operations, such as an OpenAPI document (dcat:endpointDescription).
     *
     * @return list of URLs
     */
    public List<String> getEndpointDescriptions()
    {
        return endpointDescriptions;
    }


    /**
     * Set up the descriptions of the service's operations, such as an OpenAPI document (dcat:endpointDescription).
     *
     * @param endpointDescriptions list of URLs
     */
    public void setEndpointDescriptions(List<String> endpointDescriptions)
    {
        this.endpointDescriptions = endpointDescriptions;
    }


    /**
     * Return the IRIs of the datasets that the service gives access to (dcat:servesDataset).
     *
     * @return list of IRIs
     */
    public List<String> getServesDatasets()
    {
        return servesDatasets;
    }


    /**
     * Set up the IRIs of the datasets that the service gives access to (dcat:servesDataset).
     *
     * @param servesDatasets list of IRIs
     */
    public void setServesDatasets(List<String> servesDatasets)
    {
        this.servesDatasets = servesDatasets;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "DcatDataService{" +
                       "endpointURLs=" + endpointURLs +
                       ", endpointDescriptions=" + endpointDescriptions +
                       ", servesDatasets=" + servesDatasets +
                       "} " + super.toString();
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
        if (this == objectToCompare)
        {
            return true;
        }
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        DcatDataService that = (DcatDataService) objectToCompare;
        return Objects.equals(endpointURLs, that.endpointURLs) &&
                       Objects.equals(endpointDescriptions, that.endpointDescriptions) &&
                       Objects.equals(servesDatasets, that.servesDatasets);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), endpointURLs, endpointDescriptions, servesDatasets);
    }
}
