/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.configuration.properties;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Collections;
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
public class RepositoryPerformanceWorkbenchConfig extends AdminServicesConfigHeader
{
    private static final long    serialVersionUID = 1L;

    private String   tutRepositoryServerName = null;
    private int      instancesPerType = 50;
    private int      maxSearchResults = 10;
    private int      waitBetweenScenarios = 60;
    private List<String> profilesToSkip = Collections.emptyList();
    private List<String> methodsToSkip  = Collections.emptyList();


    /**
     * Default constructor does nothing.
     */
    public RepositoryPerformanceWorkbenchConfig()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public RepositoryPerformanceWorkbenchConfig(RepositoryPerformanceWorkbenchConfig template)
    {
        super(template);

        if (template != null)
        {
            tutRepositoryServerName = template.getTutRepositoryServerName();
            maxSearchResults = template.getMaxSearchResults();
            waitBetweenScenarios = template.getWaitBetweenScenarios();
            profilesToSkip = template.getProfilesToSkip();
            methodsToSkip  = template.getMethodsToSkip();
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
     * Return the number of instances that should be created for each type definition.
     *
     * @return number of instances (per type)
     */
    public int getInstancesPerType()
    {
        return instancesPerType;
    }


    /**
     * Set up the number of instances that should be created for each type definition.
     *
     * @param instancesPerType number of instances (per type) to create
     */
    public void setInstancesPerType(int instancesPerType)
    {
        this.instancesPerType = instancesPerType;
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
     * Return the amount of time (in seconds) to wait between scenarios of the performance test. This would be useful
     * for example, where you want to simulate a batch-load with an eventually-consistent search index.
     *
     * @return time to wait (in seconds) between performance test scenarios
     */
    public int getWaitBetweenScenarios()
    {
        return waitBetweenScenarios;
    }


    /**
     * Set up the amount of time (in seconds) to wait between scenarios of the performance test. This would be useful
     * for example, where you want to simulate a batch-load with an eventually-consistent search index.
     *
     * @param waitBetweenScenarios amount of time (in seconds) to wait betweeen performance test scenarios
     */
    public void setWaitBetweenScenarios(int waitBetweenScenarios)
    {
        this.waitBetweenScenarios = waitBetweenScenarios;
    }


    /**
     * Return the profiles (if any) that should be skipped during the performance test.
     *
     * @return list of the profile names that should be skipped during testing
     */
    public List<String> getProfilesToSkip()
    {
        return profilesToSkip;
    }


    /**
     * Set up the profiles that should be skipped during the performance test.
     *
     * @param profilesToSkip list of profile names that should be skipped during testing
     */
    public void setProfilesToSkip(List<String> profilesToSkip)
    {
        this.profilesToSkip = profilesToSkip;
    }


    /**
     * Return the methods (if any) that should be skipped during the performance test.
     *
     * @return list of the method names that should be skipped during testing
     */
    public List<String> getMethodsToSkip()
    {
        return methodsToSkip;
    }


    /**
     * Set up the methods that should be skipped during the performance test.
     *
     * @param methodsToSkip list of method names that should be skipped during testing
     */
    public void setMethodsToSkip(List<String> methodsToSkip)
    {
        this.methodsToSkip = methodsToSkip;
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
                "instancesPerType='" + instancesPerType + '\'' +
                "maxSearchResults='" + maxSearchResults + '\'' +
                "waitBetweenScenarios='" + waitBetweenScenarios + '\'' +
                "profilesToSkip=" + profilesToSkip +
                "methodsToSkip=" + methodsToSkip +
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
        RepositoryPerformanceWorkbenchConfig that = (RepositoryPerformanceWorkbenchConfig) objectToCompare;
        return Objects.equals(getTutRepositoryServerName(), that.getTutRepositoryServerName())
                && Objects.equals(getInstancesPerType(), that.getInstancesPerType())
                && Objects.equals(getMaxSearchResults(), that.getMaxSearchResults())
                && Objects.equals(getWaitBetweenScenarios(), that.getWaitBetweenScenarios())
                && Objects.equals(getProfilesToSkip(), that.getProfilesToSkip())
                && Objects.equals(getMethodsToSkip(), that.getMethodsToSkip());
    }


    /**
     * Return a hash code based on the values of this object.
     *
     * @return in hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(getTutRepositoryServerName(), getInstancesPerType(), getMaxSearchResults(), getWaitBetweenScenarios(), getProfilesToSkip(), getMethodsToSkip());
    }
}
