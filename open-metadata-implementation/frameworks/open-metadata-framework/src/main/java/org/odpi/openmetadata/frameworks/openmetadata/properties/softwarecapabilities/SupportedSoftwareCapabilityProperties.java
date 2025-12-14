/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.softwarecapabilities;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipBeanProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Date;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * SupportedSoftwareCapabilityProperties describes the properties for the SupportedSoftwareCapability relationship between a ITInfrastructure asset
 * and a Software Capability.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class SupportedSoftwareCapabilityProperties extends RelationshipBeanProperties
{
    private Date              deploymentTime       = null;
    private String            deployer             = null;
    private String            deployerTypeName     = null;
    private String            deployerPropertyName = null;


    /**
     * Default constructor
     */
    public SupportedSoftwareCapabilityProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.SUPPORTED_SOFTWARE_CAPABILITY_RELATIONSHIP.typeName);
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public SupportedSoftwareCapabilityProperties(SupportedSoftwareCapabilityProperties template)
    {
        super(template);

        if (template != null)
        {
            deploymentTime = template.getDeploymentTime();
            deployer = template.getDeployer();
            deployerTypeName = template.getDeployerTypeName();
            deployerPropertyName = template.getDeployerPropertyName();
        }
    }


    /**
     * Return the time that the capability was deployed into the server.
     *
     * @return date/time
     */
    public Date getDeploymentTime()
    {
        return deploymentTime;
    }


    /**
     * Set up the time that the capability was deployed into the server.
     *
     * @param deploymentTime date/time
     */
    public void setDeploymentTime(Date deploymentTime)
    {
        this.deploymentTime = deploymentTime;
    }


    /**
     * Return the userId of the deployer.
     *
     * @return name
     */
    public String getDeployer()
    {
        return deployer;
    }


    /**
     * Set up the userId of the deployer.
     *
     * @param deployer name
     */
    public void setDeployer(String deployer)
    {
        this.deployer = deployer;
    }


    /**
     * Return the type name of the element used to represent the deployer.
     *
     * @return string name
     */
    public String getDeployerTypeName()
    {
        return deployerTypeName;
    }


    /**
     * Set up the type name of the element used to represent the deployer.
     *
     * @param deployerTypeName string name
     */
    public void setDeployerTypeName(String deployerTypeName)
    {
        this.deployerTypeName = deployerTypeName;
    }


    /**
     * Return the property name from the element used to represent the deployer.
     *
     * @return string name
     */
    public String getDeployerPropertyName()
    {
        return deployerPropertyName;
    }


    /**
     * Set up the property name from the element used to represent the deployer.
     *
     * @param deployerPropertyName string name
     */
    public void setDeployerPropertyName(String deployerPropertyName)
    {
        this.deployerPropertyName = deployerPropertyName;
    }


    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "SupportedSoftwareCapabilityProperties{" +
                "deploymentTime=" + deploymentTime +
                ", deployer='" + deployer + '\'' +
                ", deployerTypeName='" + deployerTypeName + '\'' +
                ", deployerPropertyName='" + deployerPropertyName + '\'' +
                "} " + super.toString();
    }


    /**
     * Equals method that returns true if containing properties are the same.
     *
     * @param objectToCompare object to compare
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
        SupportedSoftwareCapabilityProperties that = (SupportedSoftwareCapabilityProperties) objectToCompare;
        return Objects.equals(deploymentTime, that.deploymentTime) &&
                       Objects.equals(deployer, that.deployer) &&
                       Objects.equals(deployerTypeName, that.deployerTypeName) &&
                       Objects.equals(deployerPropertyName, that.deployerPropertyName);
    }


    /**
     * Hash code for this object
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), deploymentTime, deployer, deployerTypeName, deployerPropertyName);
    }
}
