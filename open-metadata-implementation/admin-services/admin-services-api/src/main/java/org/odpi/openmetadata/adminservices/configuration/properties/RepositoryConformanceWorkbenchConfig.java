/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.configuration.properties;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serial;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * RepositoryConformanceWorkbenchConfig provides the config that drives the RepositoryWorkbench within the
 * Open Metadata Conformance Suite.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class RepositoryConformanceWorkbenchConfig extends AdminServicesConfigHeader
{
    private String       tutRepositoryServerName = null;
    private int          maxSearchResults        = 50;
    private List<String> testEntityTypes         = null;


    /**
     * Default constructor does nothing.
     */
    public RepositoryConformanceWorkbenchConfig()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public RepositoryConformanceWorkbenchConfig(RepositoryConformanceWorkbenchConfig template)
    {
        super(template);

        if (template != null)
        {
            tutRepositoryServerName = template.getTutRepositoryServerName();
            maxSearchResults = template.getMaxSearchResults();
        }
    }


    /**
     * Return the name of the server that the repository workbench is to test.
     *
     * @return server name
     */
    public String getTutRepositoryServerName()
    {
        return tutRepositoryServerName;
    }


    /**
     * Set up the name of the server that the repository workbench is to test.
     *
     * @param tutRepositoryServerName server name
     */
    public void setTutRepositoryServerName(String tutRepositoryServerName)
    {
        this.tutRepositoryServerName = tutRepositoryServerName;
    }


    /**
     * Return the maximum search results that should processed for testing the search operations of the server under
     * test.
     *
     * @return page size
     */
    public int getMaxSearchResults()
    {
        return maxSearchResults;
    }


    /**
     * Set up the maximum search results that should be processed for testing the search operations of the server under
     * test.
     *
     * @param maxSearchResults page size
     */
    public void setMaxSearchResults(int maxSearchResults)
    {
        this.maxSearchResults = maxSearchResults;
    }


    /**
     * Return the list of entity types to test.  If the value is null then all known entities will be used.
     * The names of the entities are used to drive the spawning of tests since the repository workbench aims to test
     * each permutation of types.
     *
     * @return list of entity type names
     */
    public List<String> getTestEntityTypes()
    {
        return testEntityTypes;
    }


    /**
     * Return the list of entity types to test.  If the value is null then all known entities will be used.
     * The names of the entities are used to drive the spawning of tests since the repository workbench aims to test
     * each permutation of types.
     *
     * @param testEntityTypes list of entity type names (or null to test all types)
     */
    public void setTestEntityTypes(List<String> testEntityTypes)
    {
        this.testEntityTypes = testEntityTypes;
    }


    /**
     * Standard toString method.
     *
     * @return JSON style description of variables.
     */
    @Override
    public String toString()
    {
        return "ConformanceSuiteConfig{" +
                "tutRepositoryServerName='" + tutRepositoryServerName + '\'' +
                "maxSearchResults='" + maxSearchResults + '\'' +
                '}';
    }


    /**
     * Validate that an object is equal depending on their stored values.
     *
     * @param objectToCompare object
     * @return boolean result
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
        RepositoryConformanceWorkbenchConfig that = (RepositoryConformanceWorkbenchConfig) objectToCompare;
        return Objects.equals(getTutRepositoryServerName(), that.getTutRepositoryServerName())
                && Objects.equals(getMaxSearchResults(), that.getMaxSearchResults());
    }


    /**
     * Return a hash code based on the values of this object.
     *
     * @return in hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(getTutRepositoryServerName(), getMaxSearchResults());
    }
}
