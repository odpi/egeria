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
    private List<String> testRelationshipTypes   = null;
    private List<String> testClassificationTypes = null;
    private int          eventPollCount          = 300;
    private int          eventPollPeriod         = 100;


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
     * Return the number of times a test case polls while waiting for an event to be propagated and processed.
     * Together with the poll period this sets how long a test waits before deciding the event is not coming.
     * <br>
     * The budget only costs anything when propagation does not happen - a loop that gets what it is waiting
     * for leaves immediately - so it is set well above what a healthy cohort needs.  It is configurable
     * because a repository that scans rather than indexes gets slower as a run goes on, and on a loaded
     * machine that slowness reaches the point where the wait expires and ordinary delay is reported as a
     * conformance failure.  Raising it on such a machine distinguishes "not yet" from "never"; the default
     * is left where it is so that an ordinary run reports timing sensitivity rather than hiding it.
     *
     * @return number of polls
     */
    public int getEventPollCount()
    {
        return eventPollCount;
    }


    /**
     * Set up the number of times a test case polls while waiting for an event.
     *
     * @param eventPollCount number of polls
     */
    public void setEventPollCount(int eventPollCount)
    {
        this.eventPollCount = eventPollCount;
    }


    /**
     * Return the interval in milliseconds between polls while waiting for an event.
     *
     * @return milliseconds
     */
    public int getEventPollPeriod()
    {
        return eventPollPeriod;
    }


    /**
     * Set up the interval in milliseconds between polls while waiting for an event.
     *
     * @param eventPollPeriod milliseconds
     */
    public void setEventPollPeriod(int eventPollPeriod)
    {
        this.eventPollPeriod = eventPollPeriod;
    }


    /**
     * Return the list of relationship types to test.  If the value is null or empty then every relationship
     * type whose two ends are both available for testing is used.  Naming types here narrows that set - the
     * ends still have to be available, so naming a relationship whose ends are not among the entity types
     * being tested does not bring it into the run.
     *
     * @return list of relationship type names
     */
    public List<String> getTestRelationshipTypes()
    {
        return testRelationshipTypes;
    }


    /**
     * Set up the list of relationship types to test.
     *
     * @param testRelationshipTypes list of relationship type names (or null to test all available types)
     */
    public void setTestRelationshipTypes(List<String> testRelationshipTypes)
    {
        this.testRelationshipTypes = testRelationshipTypes;
    }


    /**
     * Return the list of classification types to test.  If the value is null or empty then every
     * classification type with a valid entity among those being tested is used.  Naming types here narrows
     * that set - a classification still has to have a valid entity type in the run to be testable.
     *
     * @return list of classification type names
     */
    public List<String> getTestClassificationTypes()
    {
        return testClassificationTypes;
    }


    /**
     * Set up the list of classification types to test.
     *
     * @param testClassificationTypes list of classification type names (or null to test all available types)
     */
    public void setTestClassificationTypes(List<String> testClassificationTypes)
    {
        this.testClassificationTypes = testClassificationTypes;
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
                ", testEntityTypes=" + testEntityTypes +
                ", testRelationshipTypes=" + testRelationshipTypes +
                ", testClassificationTypes=" + testClassificationTypes +
                ", eventPollCount=" + eventPollCount +
                ", eventPollPeriod=" + eventPollPeriod +
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
                && Objects.equals(getMaxSearchResults(), that.getMaxSearchResults())
                && Objects.equals(getTestEntityTypes(), that.getTestEntityTypes())
                && Objects.equals(getTestRelationshipTypes(), that.getTestRelationshipTypes())
                && Objects.equals(getTestClassificationTypes(), that.getTestClassificationTypes())
                && (getEventPollCount() == that.getEventPollCount())
                && (getEventPollPeriod() == that.getEventPollPeriod());
    }


    /**
     * Return a hash code based on the values of this object.
     *
     * @return in hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(getTutRepositoryServerName(), getMaxSearchResults(), getTestEntityTypes(),
                            getTestRelationshipTypes(), getTestClassificationTypes(),
                            getEventPollCount(), getEventPollPeriod());
    }
}
