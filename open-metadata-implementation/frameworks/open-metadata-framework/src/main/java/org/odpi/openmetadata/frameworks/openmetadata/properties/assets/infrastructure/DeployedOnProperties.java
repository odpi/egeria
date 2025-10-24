/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.assets.infrastructure;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.enums.OperationalStatus;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipBeanProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * DeployedOnProperties describes the properties for the DeployedOn relationship between an IT Infrastructure asset
 * and a destination asset.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class DeployedOnProperties extends RelationshipBeanProperties
{
    private static final String deploymentTimeProperty       = "deploymentTime";
    private static final String deployerProperty             = "deployer";
    private static final String deployerTypeNameProperty     = "deployerTypeName";
    private static final String deployerPropertyNameProperty = "deployerPropertyName";
    private static final String deploymentStatusProperty     = "deploymentStatus";


    private Date              deploymentTime       = null;
    private String            deployer             = null;
    private String            deployerTypeName     = null;
    private String            deployerPropertyName = null;
    private OperationalStatus operationalStatus    = null;


    /**
     * Default constructor
     */
    public DeployedOnProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.DEPLOYED_ON_RELATIONSHIP.typeName);
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public DeployedOnProperties(DeployedOnProperties template)
    {
        super(template);

        if (template != null)
        {
            deploymentTime = template.getDeploymentTime();
            deployer = template.getDeployer();
            deployerTypeName = template.getDeployerTypeName();
            deployerPropertyName = template.getDeployerPropertyName();
            operationalStatus    = template.getOperationalStatus();
        }
    }


    /**
     * Copy/clone constructor
     *
     * @param properties property map
     * @param effectiveFrom effective from
     * @param effectiveTo effective to
     */
    public DeployedOnProperties(Map<String, Object> properties,
                                Date                effectiveFrom,
                                Date                effectiveTo)
    {
        super();

        super.setEffectiveFrom(effectiveFrom);
        super.setEffectiveTo(effectiveTo);

        if (properties != null)
        {
            deploymentTime = (Date)properties.get(deploymentTimeProperty);
            deployer = properties.get(deployerProperty).toString();
            deployerTypeName = properties.get(deployerTypeNameProperty).toString();
            deployerPropertyName = properties.get(deployerPropertyNameProperty).toString();

            operationalStatus = OperationalStatus.DISABLED;
            int operationalStatus = (Integer)properties.get(deploymentStatusProperty);

            if (operationalStatus == 1)
            {
                this.operationalStatus = OperationalStatus.ENABLED;
            }
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

        if (operationalStatus != null)
        {
            propertyMap.put(deploymentStatusProperty, operationalStatus.getOrdinal());
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
    public OperationalStatus getOperationalStatus()
    {
        return operationalStatus;
    }


    /**
     * Set up whether the capability is ready to use.
     *
     * @param operationalStatus operational status enum
     */
    public void setOperationalStatus(OperationalStatus operationalStatus)
    {
        this.operationalStatus = operationalStatus;
    }


    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "DeployedOnProperties{" +
                       "deploymentTime=" + deploymentTime +
                       ", deployer='" + deployer + '\'' +
                       ", deploymentStatus=" + operationalStatus +
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
        DeployedOnProperties that = (DeployedOnProperties) objectToCompare;
        return Objects.equals(deploymentTime, that.deploymentTime) &&
                       Objects.equals(deployer, that.deployer) &&
                       Objects.equals(deployerTypeName, that.deployerTypeName) &&
                       Objects.equals(deployerPropertyName, that.deployerPropertyName) &&
                       operationalStatus == that.operationalStatus;
    }


    /**
     * Hash code for this object
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), deploymentTime, deployer, deployerTypeName, deployerPropertyName, operationalStatus);
    }
}
