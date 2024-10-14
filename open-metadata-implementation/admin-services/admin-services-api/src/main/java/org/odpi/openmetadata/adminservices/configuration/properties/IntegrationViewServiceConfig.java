/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.configuration.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceRegistrationEntry;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * The IntegrationViewServiceConfig class is a specialization of ViewServiceConfig for integration-level view services
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class IntegrationViewServiceConfig extends ViewServiceConfig
{
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
     * Standard toString method.
     *
     * @return JSON style description of variables.
     */
    @Override
    public String toString()
    {
        return "IntegrationViewServiceConfig{} " + super.toString();
    }
}
