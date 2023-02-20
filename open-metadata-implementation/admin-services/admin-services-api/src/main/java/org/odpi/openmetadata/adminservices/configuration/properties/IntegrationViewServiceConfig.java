/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.configuration.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceRegistrationEntry;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/*
 * The IntegrationViewServiceConfig class is a specialization of ViewServiceConfig for integration-level view services
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")

public class IntegrationViewServiceConfig extends ViewServiceConfig {

    private List<ResourceEndpointConfig> resourceEndpoints;

    /**
     * Default constructor for use with Jackson libraries
     */
    public IntegrationViewServiceConfig() {
        super();
    }

    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public IntegrationViewServiceConfig(IntegrationViewServiceConfig template)
    {
        super(template);
        if (template != null)
        {
            resourceEndpoints = template.getResourceEndpoints();
        }
    }

    /**
     * Call the superclass to set up the default values for a view service using a view service description.
     *
     * @param viewRegistration fixed properties about the view service
     */
    public IntegrationViewServiceConfig(ViewServiceRegistrationEntry viewRegistration)
    {
        super(viewRegistration);
    }



    /**
     * Return the resourceEndpoints list.
     *
     * @return displayName
     */
    public List<ResourceEndpointConfig> getResourceEndpoints()
    {
        return resourceEndpoints;
    }


    /**
     * Set the resourceEndpoints of resource.
     *
     * @param resourceEndpoints list of resource endpoint configuration objects
     */
    public void setResourceEndpoints(List<ResourceEndpointConfig> resourceEndpoints)
    {
        this.resourceEndpoints = resourceEndpoints;
    }




    /**
     * Standard toString method.
     *
     * @return JSON style description of variables.
     */
    @Override
    public String toString()
    {
        return "IntegrationViewServiceConfig{" +
                       "resourceEndpoints=" + resourceEndpoints +
                       ", viewServiceId=" + getViewServiceId() +
                       ", viewServiceDevelopmentStatus=" + getViewServiceDevelopmentStatus() +
                       ", viewServiceAdminClass='" + getViewServiceAdminClass() + '\'' +
                       ", viewServiceFullName='" + getViewServiceFullName() + '\'' +
                       ", viewServiceName='" + getViewServiceName() + '\'' +
                       ", viewServiceURLMarker='" + getViewServiceURLMarker() + '\'' +
                       ", viewServiceDescription='" + getViewServiceDescription() + '\'' +
                       ", viewServiceWiki='" + getViewServiceWiki() + '\'' +
                       ", viewServiceOperationalStatus=" + getViewServiceOperationalStatus() +
                       ", viewServiceOptions=" + getViewServiceOptions() +
                       ", OMAGServerPlatformRootURL='" + getOMAGServerPlatformRootURL() + '\'' +
                       ", OMAGServerName='" + getOMAGServerName() + '\'' +
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
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        IntegrationViewServiceConfig that = (IntegrationViewServiceConfig) objectToCompare;
        return Objects.equals(resourceEndpoints, that.resourceEndpoints);
    }


    /**
     * Return a hash code based on the values of this object.
     *
     * @return in hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getResourceEndpoints());
    }

}
