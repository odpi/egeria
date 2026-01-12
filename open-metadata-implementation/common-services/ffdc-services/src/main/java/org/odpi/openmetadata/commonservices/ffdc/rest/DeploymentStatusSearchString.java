/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.commonservices.ffdc.rest;

import org.odpi.openmetadata.frameworks.openmetadata.enums.DeploymentStatus;

import java.util.Objects;

/**
 * A request body that allows the status of an authored referenceable to be included in the search criteria.
 */
public class DeploymentStatusSearchString extends SearchStringRequestBody
{
    private DeploymentStatus deploymentStatus = null;

    /**
     * Default constructor
     */
    public DeploymentStatusSearchString()
    {
    }

    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public DeploymentStatusSearchString(DeploymentStatusSearchString template)
    {
        super(template);

        if (template != null)
        {
            deploymentStatus = template.getDeploymentStatus();
        }
    }


    /**
     * Return the status value.
     *
     * @return element status enum value
     */
    public DeploymentStatus getDeploymentStatus()
    {
        return deploymentStatus;
    }


    /**
     * Set up the status value.
     *
     * @param deploymentStatus element status enum value
     */
    public void setDeploymentStatus(DeploymentStatus deploymentStatus)
    {
        this.deploymentStatus = deploymentStatus;
    }


    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "DeploymentStatusSearchString{" +
                "deploymentStatus=" + deploymentStatus +
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
        DeploymentStatusSearchString that = (DeploymentStatusSearchString) objectToCompare;
        return Objects.equals(deploymentStatus, that.deploymentStatus);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), deploymentStatus);
    }
}
