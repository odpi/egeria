/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.surveyreports;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * SecurityPostureAnnotationProperties describes a summary of the findings and coverage of one run of a security test.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class SecurityPostureAnnotationProperties extends ResourceMeasureAnnotationProperties
{
    private Map<String, Long> findingCountsBySeverity = null;
    private long              resourcesInScopeCount   = 0;
    private long              resourcesTestedCount    = 0;
    private long              newFindingCount         = 0;
    private long              resolvedFindingCount    = 0;


    /**
     * Default constructor
     */
    public SecurityPostureAnnotationProperties()
    {
        super();
        super.typeName = OpenMetadataType.SECURITY_POSTURE_ANNOTATION.typeName;
    }


    /**
     * Copy clone constructor
     *
     * @param template object to copy
     */
    public SecurityPostureAnnotationProperties(SecurityPostureAnnotationProperties template)
    {
        super(template);

        if (template != null)
        {
            this.findingCountsBySeverity = template.getFindingCountsBySeverity();
            this.resourcesInScopeCount   = template.getResourcesInScopeCount();
            this.resourcesTestedCount    = template.getResourcesTestedCount();
            this.newFindingCount         = template.getNewFindingCount();
            this.resolvedFindingCount    = template.getResolvedFindingCount();
        }
    }


    /**
     * Return the number of open findings for each severity.
     *
     * @return map
     */
    public Map<String, Long> getFindingCountsBySeverity()
    {
        return findingCountsBySeverity;
    }


    /**
     * Set up the number of open findings for each severity.
     *
     * @param findingCountsBySeverity map
     */
    public void setFindingCountsBySeverity(Map<String, Long> findingCountsBySeverity)
    {
        this.findingCountsBySeverity = findingCountsBySeverity;
    }


    /**
     * Return the number of resources in the scope of the security test.
     *
     * @return long
     */
    public long getResourcesInScopeCount()
    {
        return resourcesInScopeCount;
    }


    /**
     * Set up the number of resources in the scope of the security test.
     *
     * @param resourcesInScopeCount long
     */
    public void setResourcesInScopeCount(long resourcesInScopeCount)
    {
        this.resourcesInScopeCount = resourcesInScopeCount;
    }


    /**
     * Return the number of resources in scope that were tested.
     *
     * @return long
     */
    public long getResourcesTestedCount()
    {
        return resourcesTestedCount;
    }


    /**
     * Set up the number of resources in scope that were tested.
     *
     * @param resourcesTestedCount long
     */
    public void setResourcesTestedCount(long resourcesTestedCount)
    {
        this.resourcesTestedCount = resourcesTestedCount;
    }


    /**
     * Return the number of findings detected for the first time in this run.
     *
     * @return long
     */
    public long getNewFindingCount()
    {
        return newFindingCount;
    }


    /**
     * Set up the number of findings detected for the first time in this run.
     *
     * @param newFindingCount long
     */
    public void setNewFindingCount(long newFindingCount)
    {
        this.newFindingCount = newFindingCount;
    }


    /**
     * Return the number of findings from earlier runs that are no longer detected.
     *
     * @return long
     */
    public long getResolvedFindingCount()
    {
        return resolvedFindingCount;
    }


    /**
     * Set up the number of findings from earlier runs that are no longer detected.
     *
     * @param resolvedFindingCount long
     */
    public void setResolvedFindingCount(long resolvedFindingCount)
    {
        this.resolvedFindingCount = resolvedFindingCount;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "SecurityPostureAnnotationProperties{" +
                "findingCountsBySeverity=" + findingCountsBySeverity +
                ", resourcesInScopeCount=" + resourcesInScopeCount +
                ", resourcesTestedCount=" + resourcesTestedCount +
                ", newFindingCount=" + newFindingCount +
                ", resolvedFindingCount=" + resolvedFindingCount +
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
        SecurityPostureAnnotationProperties that = (SecurityPostureAnnotationProperties) objectToCompare;
        return Objects.equals(findingCountsBySeverity, that.findingCountsBySeverity) &&
                resourcesInScopeCount == that.resourcesInScopeCount &&
                resourcesTestedCount == that.resourcesTestedCount &&
                newFindingCount == that.newFindingCount &&
                resolvedFindingCount == that.resolvedFindingCount;
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), findingCountsBySeverity, resourcesInScopeCount, resourcesTestedCount, newFindingCount, resolvedFindingCount);
    }
}
