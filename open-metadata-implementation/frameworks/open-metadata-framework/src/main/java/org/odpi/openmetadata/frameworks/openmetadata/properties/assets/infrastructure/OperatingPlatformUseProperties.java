/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.assets.infrastructure;

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
 * OperatingPlatformUseProperties describes the properties for the OperatingPlatformUse relationship between an operating platform
 * and the IT infrastructure that it is installed on.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OperatingPlatformUseProperties extends RelationshipBeanProperties
{
    private Date   installTime          = null;
    private String deployer             = null;
    private String deployerTypeName     = null;
    private String deployerPropertyName = null;


    /**
     * Default constructor
     */
    public OperatingPlatformUseProperties()
    {
        super();
        super.typeName = OpenMetadataType.OPERATING_PLATFORM_USE_RELATIONSHIP.typeName;
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public OperatingPlatformUseProperties(OperatingPlatformUseProperties template)
    {
        super(template);

        if (template != null)
        {
            installTime          = template.getInstallTime();
            deployer             = template.getDeployer();
            deployerTypeName     = template.getDeployerTypeName();
            deployerPropertyName = template.getDeployerPropertyName();
        }
    }


    /**
     * Return the time that the software was installed on the IT infrastructure.
     *
     * @return date/time
     */
    public Date getInstallTime()
    {
        return installTime;
    }


    /**
     * Set up the time that the software was installed on the IT infrastructure.
     *
     * @param installTime date/time
     */
    public void setInstallTime(Date installTime)
    {
        this.installTime = installTime;
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
        return "OperatingPlatformUseProperties{" +
                "installTime=" + installTime +
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
        OperatingPlatformUseProperties that = (OperatingPlatformUseProperties) objectToCompare;
        return Objects.equals(installTime, that.installTime) &&
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
        return Objects.hash(super.hashCode(), installTime, deployer, deployerTypeName, deployerPropertyName);
    }
}
