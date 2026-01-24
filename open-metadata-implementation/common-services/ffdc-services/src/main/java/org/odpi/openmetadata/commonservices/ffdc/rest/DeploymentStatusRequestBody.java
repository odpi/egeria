/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.commonservices.ffdc.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.enums.DeploymentStatus;
import org.odpi.openmetadata.frameworks.openmetadata.search.QueryOptions;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * DeploymentStatusRequestBody provides a structure for passing the status.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class DeploymentStatusRequestBody extends QueryOptions
{
    private List<DeploymentStatus> deploymentStatusList = null;


    /**
     * Default constructor
     */
    public DeploymentStatusRequestBody()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public DeploymentStatusRequestBody(DeploymentStatusRequestBody template)
    {
        super(template);

        if (template != null)
        {
            deploymentStatusList = template.getDeploymentStatusList();
        }
    }


    /**
     * Return the status value.
     *
     * @return status enum list
     */
    public List<DeploymentStatus> getDeploymentStatusList()
    {
        return deploymentStatusList;
    }


    /**
     * Set up the status value.
     *
     * @param deploymentStatusList status enum list
     */
    public void setDeploymentStatusList(List<DeploymentStatus> deploymentStatusList)
    {
        this.deploymentStatusList = deploymentStatusList;
    }


    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "DeploymentStatusRequestBody{" +
                "deploymentStatusList=" + deploymentStatusList +
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
        if (this == objectToCompare) return true;
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        if (!super.equals(objectToCompare)) return false;
        DeploymentStatusRequestBody that = (DeploymentStatusRequestBody) objectToCompare;
        return deploymentStatusList == that.deploymentStatusList;
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), deploymentStatusList);
    }
}
