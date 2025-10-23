/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.surveyreports;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * DataProfileLogAnnotation is an annotation that is used when the profile data generated is too big to store in the metadata server
 * and has been located in log files instead.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ResourceProfileLogAnnotationProperties extends DataFieldAnnotationProperties
{
    private List<String> resourceProfileLogGUIDs = null;

    /**
     * Default constructor
     */
    public ResourceProfileLogAnnotationProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.RESOURCE_PROFILE_LOG_ANNOTATION.typeName);
    }


    /**
     * Copy clone constructor
     *
     * @param template object to copy
     */
    public ResourceProfileLogAnnotationProperties(ResourceProfileLogAnnotationProperties template)
    {
        super(template);

        if (template != null)
        {
            resourceProfileLogGUIDs = template.getResourceProfileLogGUIDs();
        }
    }


    /**
     * Return the names of the log files used to store the profile data.
     *
     * @return list of guid of log files
     */
    public List<String> getResourceProfileLogGUIDs()
    {
        return resourceProfileLogGUIDs;
    }


    /**
     * Set up the description of the log files used to store the profile data.
     *
     * @param resourceProfileLogFileNames list of guids of log files
     */
    public void setResourceProfileLogGUIDs(List<String> resourceProfileLogFileNames)
    {
        this.resourceProfileLogGUIDs = resourceProfileLogFileNames;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "ResourceProfileLogAnnotationProperties{" +
                "resourceProfileLogFiles=" + resourceProfileLogGUIDs +
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
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        ResourceProfileLogAnnotationProperties that = (ResourceProfileLogAnnotationProperties) objectToCompare;
        return Objects.equals(resourceProfileLogGUIDs, that.resourceProfileLogGUIDs);
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), resourceProfileLogGUIDs);
    }
}
