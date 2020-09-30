/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.configuration.properties;

import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceRegistration;

import java.util.Objects;

/*
 * The SolutionViewServiceConfig class is a specialization of ViewServiceConfig for solution-level view services
 */
public class SolutionViewServiceConfig extends ViewServiceConfig {

    /* There are no additional properties yet */

    /**
     * Default constructor for use with Jackson libraries
     */
    public SolutionViewServiceConfig() {
        super();
    }

    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public SolutionViewServiceConfig(SolutionViewServiceConfig template)
    {
        super(template);
        /* There is nothing else to copy yet... */
    }

    /**
     * Call the superclass to set up the default values for a view service using a view service description.
     *
     * @param viewRegistration fixed properties about the view service
     */
    public SolutionViewServiceConfig(ViewServiceRegistration viewRegistration)
    {
        super(viewRegistration);
    }



    /**
     * Standard toString method.
     *
     * @return JSON style description of variables.
     */
    @Override
    public String toString()
    {
        return "SolutionViewServiceConfig{" +
                "viewServiceId=" + getViewServiceId() +
                ", viewServiceAdminClass='" + getViewServiceAdminClass() + '\'' +
                ", viewServiceName='" + getViewServiceName() + '\'' +
                ", viewServiceFullName='" + getViewServiceFullName() + '\'' +
                ", viewServiceURLMarker='" + getViewServiceURLMarker() + '\'' +
                ", viewServiceDescription='" + getViewServiceDescription() + '\'' +
                ", viewServiceWiki='" + getViewServiceWiki() + '\'' +
                ", viewServiceOperationalStatus=" + getViewServiceOperationalStatus() +
                ", viewServiceOptions=" + getViewServiceOptions() +
                '}';
    }


    /**
     * Validate that an object is equal depending on their stored values.
     *
     * @param objectToCompare object
     * @return boolean result
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
        SolutionViewServiceConfig that = (SolutionViewServiceConfig) objectToCompare;
        return getViewServiceId() == that.getViewServiceId() &&
                Objects.equals(getViewServiceAdminClass(), that.getViewServiceAdminClass()) &&
                Objects.equals(getViewServiceName(), that.getViewServiceName()) &&
                Objects.equals(getViewServiceFullName(), that.getViewServiceFullName()) &&
                Objects.equals(getViewServiceURLMarker(), that.getViewServiceURLMarker()) &&
                Objects.equals(getViewServiceDescription(), that.getViewServiceDescription()) &&
                Objects.equals(getViewServiceWiki(), that.getViewServiceWiki()) &&
                getViewServiceOperationalStatus() == that.getViewServiceOperationalStatus() &&
                Objects.equals(getViewServiceOptions(), that.getViewServiceOptions());
    }


    /**
     * Return a hash code based on the values of this object.
     *
     * @return in hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode());
    }

}
