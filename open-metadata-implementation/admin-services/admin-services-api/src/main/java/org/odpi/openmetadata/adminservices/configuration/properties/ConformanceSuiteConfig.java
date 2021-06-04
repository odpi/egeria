/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.configuration.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ConformanceSuiteConfig provides the configuration for an OMAG server that is running the Open Metadata
 * Conformance suite.  It supports two workbenches - one that tests the repository services in a server connected
 * to the same cohort as the conformance suite server - the other tests the platform services of a platform given the
 * server URL root of the platform.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ConformanceSuiteConfig extends AdminServicesConfigHeader
{
    private static final long    serialVersionUID = 1L;

    private PlatformConformanceWorkbenchConfig   platformWorkbenchConfig     = null;
    private RepositoryConformanceWorkbenchConfig repositoryWorkbenchConfig   = null;
    private RepositoryPerformanceWorkbenchConfig repositoryPerformanceConfig = null;


    /**
     * Default constructor does nothing.
     */
    public ConformanceSuiteConfig()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ConformanceSuiteConfig(ConformanceSuiteConfig template)
    {
        super(template);

        if (template != null)
        {
            platformWorkbenchConfig = template.getPlatformWorkbenchConfig();
            repositoryWorkbenchConfig = template.getRepositoryWorkbenchConfig();
            repositoryPerformanceConfig = template.getRepositoryPerformanceConfig();
        }
    }


    /**
     * Return the configuration for the platform workbench.
     *
     * @return platform workbench config properties
     */
    public PlatformConformanceWorkbenchConfig getPlatformWorkbenchConfig()
    {
        return platformWorkbenchConfig;
    }


    /**
     * Set up the configuration for the platform workbench.
     *
     * @param platformWorkbenchConfig platform workbench config properties
     */
    public void setPlatformWorkbenchConfig(PlatformConformanceWorkbenchConfig platformWorkbenchConfig)
    {
        this.platformWorkbenchConfig = platformWorkbenchConfig;
    }


    /**
     * Return the configuration for the repository workbench.
     *
     * @return repository workbench config properties
     */
    public RepositoryConformanceWorkbenchConfig getRepositoryWorkbenchConfig()
    {
        return repositoryWorkbenchConfig;
    }


    /**
     * Set up the configuration for the repository workbench.
     *
     * @param repositoryWorkbenchConfig repository workbench config properties
     */
    public void setRepositoryWorkbenchConfig(RepositoryConformanceWorkbenchConfig repositoryWorkbenchConfig)
    {
        this.repositoryWorkbenchConfig = repositoryWorkbenchConfig;
    }


    /**
     * Return the configuration for the repository performance workbench.
     *
     * @return repository performance workbench config properties
     */
    public RepositoryPerformanceWorkbenchConfig getRepositoryPerformanceConfig()
    {
        return repositoryPerformanceConfig;
    }


    /**
     * Set up the configuration for the repository performance workbench.
     *
     * @param repositoryPerformanceConfig repository performance workbench config properties
     */
    public void setRepositoryPerformanceConfig(RepositoryPerformanceWorkbenchConfig repositoryPerformanceConfig)
    {
        this.repositoryPerformanceConfig = repositoryPerformanceConfig;
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
                ", platformWorkbenchConfig=" + platformWorkbenchConfig +
                ", repositoryWorkbenchConfig=" + repositoryWorkbenchConfig +
                ", repositoryWorkbenchConfig=" + repositoryPerformanceConfig +
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
        ConformanceSuiteConfig that = (ConformanceSuiteConfig) objectToCompare;
        return Objects.equals(getPlatformWorkbenchConfig(), that.getPlatformWorkbenchConfig()) &&
                Objects.equals(getRepositoryWorkbenchConfig(), that.getRepositoryWorkbenchConfig()) &&
                Objects.equals(getRepositoryPerformanceConfig(), that.getRepositoryPerformanceConfig());
    }


    /**
     * Return a hash code based on the values of this object.
     *
     * @return in hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(getPlatformWorkbenchConfig(), getRepositoryWorkbenchConfig(), getRepositoryPerformanceConfig());
    }
}
