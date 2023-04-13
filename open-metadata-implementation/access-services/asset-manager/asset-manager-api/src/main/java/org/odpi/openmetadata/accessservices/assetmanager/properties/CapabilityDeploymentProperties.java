/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetmanager.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serial;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * CapabilityDeploymentProperties describes the properties for the SupportedSoftwareCapability relationship between a ITInfrastructure asset
 * and a Software Capability.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class CapabilityDeploymentProperties extends RelationshipProperties
{
    @Serial
    private static final long serialVersionUID = 1L;

    private static final String deploymentTimeProperty       = "deploymentTime";
    private static final String deployerProperty             = "deployer";
    private static final String deployerTypeNameProperty     = "deployerTypeName";
    private static final String deployerPropertyNameProperty = "deployerPropertyName";
    private static final String deploymentStatusProperty     = "serverCapabilityStatus";


    private Date              deploymentTime           = null;
    private String            deployer                 = null;
    private String            deployerTypeName         = null;
    private String            deployerPropertyName     = null;
    private OperationalStatus softwareCapabilityStatus = null;


    /**
     * Default constructor
     */
    public CapabilityDeploymentProperties()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public CapabilityDeploymentProperties(CapabilityDeploymentProperties template)
    {
        super(template);

        if (template != null)
        {
            deploymentTime = template.getDeploymentTime();
            deployer = template.getDeployer();
            deployerTypeName = template.getDeployerTypeName();
            deployerPropertyName = template.getDeployerPropertyName();
            softwareCapabilityStatus = template.getSoftwareCapabilityStatus();
        }
    }


    /**
     * Turn the properties into a property map.
     *
     * @return property map.
     */
    public Map<String, Object> cloneToMap()
    {
        Map<String, Object> propertyMap = new HashMap<>();

        if (deploymentTime != null)
        {
            propertyMap.put(deploymentTimeProperty, deploymentTime);
        }

        if (deployer != null)
        {
            propertyMap.put(deployerProperty, deployer);
        }

        if (deployerTypeName != null)
        {
            propertyMap.put(deployerTypeNameProperty, deployerTypeName);
        }

        if (deployerPropertyName != null)
        {
            propertyMap.put(deployerPropertyNameProperty, deployerPropertyName);
        }

        if (softwareCapabilityStatus != null)
        {
            propertyMap.put(deploymentStatusProperty, softwareCapabilityStatus.getOpenTypeOrdinal());
        }

        if (! propertyMap.isEmpty())
        {
            propertyMap = null;
        }

        return propertyMap;
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
     * Return whether the capability is ready to use.
     *
     * @return operational status enum
     */
    public OperationalStatus getSoftwareCapabilityStatus()
    {
        return softwareCapabilityStatus;
    }


    /**
     * Set up whether the capability is ready to use.
     *
     * @param softwareCapabilityStatus operational status enum
     */
    public void setSoftwareCapabilityStatus(OperationalStatus softwareCapabilityStatus)
    {
        this.softwareCapabilityStatus = softwareCapabilityStatus;
    }


    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "CapabilityDeploymentProperties{" +
                       "deploymentTime=" + deploymentTime +
                       ", deployer='" + deployer + '\'' +
                       ", deployerTypeName='" + deployerTypeName + '\'' +
                       ", deployerPropertyName='" + deployerPropertyName + '\'' +
                       ", softwareCapabilityStatus=" + softwareCapabilityStatus +
                       ", effectiveFrom=" + getEffectiveFrom() +
                       ", effectiveTo=" + getEffectiveTo() +
                       '}';
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
        CapabilityDeploymentProperties that = (CapabilityDeploymentProperties) objectToCompare;
        return Objects.equals(deploymentTime, that.deploymentTime) &&
                       Objects.equals(deployer, that.deployer) &&
                       Objects.equals(deployerTypeName, that.deployerTypeName) &&
                       Objects.equals(deployerPropertyName, that.deployerPropertyName) &&
                       softwareCapabilityStatus == that.softwareCapabilityStatus;
    }


    /**
     * Hash code for this object
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), deploymentTime, deployer, deployerTypeName, deployerPropertyName, softwareCapabilityStatus);
    }
}
