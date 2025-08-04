/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.solutions;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ReferenceableProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * SolutionBlueprintProperties represents a collection of logical architectural components that are part of the digital landscape.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class SolutionBlueprintProperties extends ReferenceableProperties
{
    private String userDefinedStatus = null;

    /**
     * Default constructor
     */
    public SolutionBlueprintProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.SOLUTION_BLUEPRINT.typeName);
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public SolutionBlueprintProperties(SolutionBlueprintProperties template)
    {
        super(template);

        if (template != null)
        {
            this.userDefinedStatus = template.getUserDefinedStatus();
        }
    }


    /**
     * Return the status of the element.
     *
     * @return string
     */
    public String getUserDefinedStatus()
    {
        return userDefinedStatus;
    }


    /**
     * Set up the status of the element
     *
     * @param userDefinedStatus string
     */
    public void setUserDefinedStatus(String userDefinedStatus)
    {
        this.userDefinedStatus = userDefinedStatus;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "SolutionBlueprintProperties{" +
                "userDefinedStatus=" + userDefinedStatus +
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
        if (! (objectToCompare instanceof SolutionBlueprintProperties that))
        {
            return false;
        }
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        return Objects.equals(userDefinedStatus, that.userDefinedStatus);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), userDefinedStatus);
    }
}
