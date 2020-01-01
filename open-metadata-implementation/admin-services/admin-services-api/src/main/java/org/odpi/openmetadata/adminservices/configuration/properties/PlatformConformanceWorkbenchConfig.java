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
 * PlatformConformanceWorkbenchConfig provides the config that drives the PlatformWorkbench within the
 * Open Metadata Conformance Suite.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class PlatformConformanceWorkbenchConfig extends AdminServicesConfigHeader
{
    private static final long    serialVersionUID = 1L;

    private String   tutPlatformURLRoot      = null;


    /**
     * Default constructor does nothing.
     */
    public PlatformConformanceWorkbenchConfig()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public PlatformConformanceWorkbenchConfig(PlatformConformanceWorkbenchConfig template)
    {
        super(template);

        if (template != null)
        {
            tutPlatformURLRoot = template.getTutPlatformURLRoot();
        }
    }


    /**
     * Return the URL root of the platform that the platform workbench is to test.
     *
     * @return URL root
     */
    public String getTutPlatformURLRoot()
    {
        return tutPlatformURLRoot;
    }


    /**
     * Set up the URL root of the platform that the platform workbench is to test.
     *
     * @param tutPlatformURLRoot URL root
     */
    public void setTutPlatformURLRoot(String tutPlatformURLRoot)
    {
        this.tutPlatformURLRoot = tutPlatformURLRoot;
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
                ", tutPlatformURLRoot='" + tutPlatformURLRoot + '\'' +
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
        PlatformConformanceWorkbenchConfig that = (PlatformConformanceWorkbenchConfig) objectToCompare;
        return Objects.equals(getTutPlatformURLRoot(), that.getTutPlatformURLRoot());
    }


    /**
     * Return a hash code based on the values of this object.
     *
     * @return in hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(getTutPlatformURLRoot());
    }
}
