/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.solutions;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.designmodels.DesignModelBoundaryProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * SolutionBoundaryProperties represents a collection of solution components and roles that are grouped together
 * within a solution blueprint to help understanding.  Solution boundaries may be nested.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class SolutionBoundaryProperties extends DesignModelBoundaryProperties
{
    /**
     * Default constructor
     */
    public SolutionBoundaryProperties()
    {
        super();
        super.typeName = OpenMetadataType.SOLUTION_BOUNDARY.typeName;
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public SolutionBoundaryProperties(SolutionBoundaryProperties template)
    {
        super(template);
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "SolutionBoundaryProperties{" +
                "} " + super.toString();
    }
}
