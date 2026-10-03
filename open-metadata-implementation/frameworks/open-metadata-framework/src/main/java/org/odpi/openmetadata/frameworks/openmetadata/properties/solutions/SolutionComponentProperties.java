/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.solutions;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.enums.DeploymentStatus;
import org.odpi.openmetadata.frameworks.openmetadata.properties.designmodels.DesignModelElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * SolutionComponentProperties represents a logical architectural component that is part of the digital landscape.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class SolutionComponentProperties extends DesignModelElementProperties
{
    private String solutionComponentType             = null;
    private String plannedDeployedImplementationType = null;
    private DeploymentStatus deploymentStatus            = null;
    private String           userDefinedDeploymentStatus       = null;


    /**
     * Default constructor
     */
    public SolutionComponentProperties()
    {
        super();
        super.typeName = OpenMetadataType.SOLUTION_COMPONENT.typeName;
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public SolutionComponentProperties(SolutionComponentProperties template)
    {
        super(template);

        if (template != null)
        {
            this.solutionComponentType             = template.getSolutionComponentType();
            this.plannedDeployedImplementationType = template.getPlannedDeployedImplementationType();
            this.deploymentStatus                  = template.getDeploymentStatus();
            this.userDefinedDeploymentStatus             = template.getUserDefinedDeploymentStatus();
        }
    }


    /**
     * Return the type of the component.
     *
     * @return string
     */
    public String getSolutionComponentType()
    {
        return solutionComponentType;
    }


    /**
     * Set up the type of the component.
     *
     * @param solutionComponentType string
     */
    public void setSolutionComponentType(String solutionComponentType)
    {
        this.solutionComponentType = solutionComponentType;
    }


    /**
     * Return which type of software component is likely to be deployed to implement this solution component.
     *
     * @return string
     */
    public String getPlannedDeployedImplementationType()
    {
        return plannedDeployedImplementationType;
    }


    /**
     * Set up which type of software component is likely to be deployed to implement this solution component.
     *
     * @param plannedDeployedImplementationType string
     */
    public void setPlannedDeployedImplementationType(String plannedDeployedImplementationType)
    {
        this.plannedDeployedImplementationType = plannedDeployedImplementationType;
    }


    /**
     * Return the deployment status of the solution component.  If it is not set, the component is assumed to be
     * deployed and active.
     *
     * @return enum
     */
    public DeploymentStatus getDeploymentStatus()
    {
        return deploymentStatus;
    }


    /**
     * Set up the deployment status of the solution component.
     *
     * @param deploymentStatus enum
     */
    public void setDeploymentStatus(DeploymentStatus deploymentStatus)
    {
        this.deploymentStatus = deploymentStatus;
    }


    /**
     * Return the locally defined deployment status.  It is used when the deploymentStatus is set to OTHER.
     *
     * @return string
     */
    public String getUserDefinedDeploymentStatus()
    {
        return userDefinedDeploymentStatus;
    }


    /**
     * Set up the locally defined deployment status.  It is used when the deploymentStatus is set to OTHER.
     *
     * @param userDefinedDeploymentStatus string
     */
    public void setUserDefinedDeploymentStatus(String userDefinedDeploymentStatus)
    {
        this.userDefinedDeploymentStatus = userDefinedDeploymentStatus;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "SolutionComponentProperties{" +
                "solutionComponentType='" + solutionComponentType + '\'' +
                ", plannedDeployedImplementationType='" + plannedDeployedImplementationType + '\'' +
                ", deploymentStatus=" + deploymentStatus +
                ", userDefinedDeploymentStatus='" + userDefinedDeploymentStatus + '\'' +
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
        if (! (objectToCompare instanceof SolutionComponentProperties that))
        {
            return false;
        }
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        return Objects.equals(solutionComponentType, that.solutionComponentType) &&
                Objects.equals(plannedDeployedImplementationType, that.plannedDeployedImplementationType) &&
                deploymentStatus == that.deploymentStatus &&
                Objects.equals(userDefinedDeploymentStatus, that.userDefinedDeploymentStatus);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), solutionComponentType, plannedDeployedImplementationType, deploymentStatus, userDefinedDeploymentStatus);
    }
}
