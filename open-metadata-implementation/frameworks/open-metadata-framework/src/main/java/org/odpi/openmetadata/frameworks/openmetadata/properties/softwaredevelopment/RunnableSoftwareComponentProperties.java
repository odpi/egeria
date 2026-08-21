/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.properties.softwaredevelopment;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.AssetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * RunnableSoftwareComponentProperties describes a software component that is released and available for use.
 * It is executable, but may have external dependencies that need to be available before it will run successfully.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class RunnableSoftwareComponentProperties extends AssetProperties
{
    private String runtimeEnvironmentType = null;


    /**
     * Default constructor
     */
    public RunnableSoftwareComponentProperties()
    {
        super();
        super.typeName = OpenMetadataType.RUNNABLE_SOFTWARE_COMPONENT.typeName;
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public RunnableSoftwareComponentProperties(RunnableSoftwareComponentProperties template)
    {
        super(template);

        if (template != null)
        {
            runtimeEnvironmentType = template.getRuntimeEnvironmentType();
        }
    }


    /**
     * Return the type of runtime environment needed to execute this software component.
     *
     * @return string
     */
    public String getRuntimeEnvironmentType()
    {
        return runtimeEnvironmentType;
    }


    /**
     * Set up the type of runtime environment needed to execute this software component.
     *
     * @param runtimeEnvironmentType string
     */
    public void setRuntimeEnvironmentType(String runtimeEnvironmentType)
    {
        this.runtimeEnvironmentType = runtimeEnvironmentType;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "RunnableSoftwareComponentProperties{" +
                "runtimeEnvironmentType='" + runtimeEnvironmentType + '\'' +
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
        RunnableSoftwareComponentProperties that = (RunnableSoftwareComponentProperties) objectToCompare;
        return Objects.equals(runtimeEnvironmentType, that.runtimeEnvironmentType);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), runtimeEnvironmentType);
    }
}
