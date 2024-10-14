/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.assets.infrastructure;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.odpi.openmetadata.frameworks.openmetadata.enums.OperationalStatus;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipProperties;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * PlatformDeploymentProperties describes the properties for the SoftwareServerPlatformDeployment relationship between a Software Server Platform
 * and a Host.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class PlatformDeploymentProperties extends RelationshipProperties
{
    private static final String deploymentTimePropertyName           = "deploymentTime";
    private static final String deployerPropertyName                 = "deployer";
    private static final String platformDeploymentStatusPropertyName = "platformStatus";

    private Date              deploymentTime           = null;
    private String            deployer                 = null;
    private OperationalStatus platformDeploymentStatus = null;


    /**
     * Default constructor
     */
    public PlatformDeploymentProperties()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public PlatformDeploymentProperties(PlatformDeploymentProperties template)
    {
        super(template);

        if (template != null)
        {
            deploymentTime = template.getDeploymentTime();
            deployer = template.getDeployer();
            platformDeploymentStatus = template.getPlatformDeploymentStatus();
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
            propertyMap.put(deploymentTimePropertyName, deploymentTime);
        }

        if (deployer != null)
        {
            propertyMap.put(deployerPropertyName, deployer);
        }

        if (platformDeploymentStatus != null)
        {
            propertyMap.put(platformDeploymentStatusPropertyName, platformDeploymentStatus.getOrdinal());
        }

        if (! propertyMap.isEmpty())
        {
            propertyMap = null;
        }

        return propertyMap;
    }


    /**
     * Return the time that the platform was deployed into the host.
     *
     * @return date/time
     */
    public Date getDeploymentTime()
    {
        return deploymentTime;
    }


    /**
     * Set up the time that the platform was deployed into the host.
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
     * Return whether the platform is ready to use.
     *
     * @return operational status enum
     */
    public OperationalStatus getPlatformDeploymentStatus()
    {
        return platformDeploymentStatus;
    }


    /**
     * Set up  whether the platform is ready to use.
     *
     * @param platformDeploymentStatus operational status enum
     */
    public void setPlatformDeploymentStatus(OperationalStatus platformDeploymentStatus)
    {
        this.platformDeploymentStatus = platformDeploymentStatus;
    }


    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "PlatformDeploymentProperties{" +
                       "deploymentTime=" + deploymentTime +
                       ", deployer='" + deployer + '\'' +
                       ", platformDeploymentStatus=" + platformDeploymentStatus +
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
        PlatformDeploymentProperties that = (PlatformDeploymentProperties) objectToCompare;
        return Objects.equals(deploymentTime, that.deploymentTime) &&
                       Objects.equals(deployer, that.deployer) &&
                       platformDeploymentStatus == that.platformDeploymentStatus;
    }


    /**
     * Hash code for this object
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), deploymentTime, deployer, platformDeploymentStatus);
    }
}
