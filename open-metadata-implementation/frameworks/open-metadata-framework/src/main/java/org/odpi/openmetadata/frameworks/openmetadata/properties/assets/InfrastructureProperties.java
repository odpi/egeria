/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.properties.assets;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.frameworks.openmetadata.enums.DeploymentStatus;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.infrastructure.ITInfrastructureProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * InfrastructureProperties is a java bean used to create assets associated with the Infrastructure.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes({
        @JsonSubTypes.Type(value = ITInfrastructureProperties.class, name = "ITInfrastructureProperties"),


})
public class InfrastructureProperties extends AssetProperties
{
    private DeploymentStatus deploymentStatus            = null;
    private String           userDefinedDeploymentStatus = null;

    /**
     * Default constructor
     */
    public InfrastructureProperties()
    {
        super();
        super.typeName = OpenMetadataType.INFRASTRUCTURE.typeName;
    }



    /**
     * Copy/clone constructor.  Note, this is a deep copy
     *
     * @param template object to copy
     */
    public InfrastructureProperties(InfrastructureProperties template)
    {
        super(template);

        if (template != null)
        {
            deploymentStatus            = template.getDeploymentStatus();
            userDefinedDeploymentStatus = template.getUserDefinedDeploymentStatus();
        }
    }

    /**
     * Copy/clone constructor.  Note, this is a deep copy
     *
     * @param template object to copy
     */
    public InfrastructureProperties(AssetProperties template)
    {
        super(template);
    }


    /**
     * Return the status of the content.
     *
     * @return status enum
     */
    public DeploymentStatus getDeploymentStatus()
    {
        return deploymentStatus;
    }


    /**
     * Set up the status of the content.
     *
     * @param deploymentStatus status enum
     */
    public void setDeploymentStatus(DeploymentStatus deploymentStatus)
    {
        this.deploymentStatus = deploymentStatus;
    }


    /**
     * Return additionally defined content statuses.
     *
     * @return string
     */
    public String getUserDefinedDeploymentStatus()
    {
        return userDefinedDeploymentStatus;
    }


    /**
     * Set up additionally defined content statuses.
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
        return "InfrastructureProperties{" +
                "deploymentStatus=" + deploymentStatus +
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
        if (this == objectToCompare) return true;
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        if (!super.equals(objectToCompare)) return false;
        InfrastructureProperties that = (InfrastructureProperties) objectToCompare;
        return  deploymentStatus == that.deploymentStatus &&
                Objects.equals(userDefinedDeploymentStatus, that.userDefinedDeploymentStatus);
    }

    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), deploymentStatus, userDefinedDeploymentStatus);
    }
}
