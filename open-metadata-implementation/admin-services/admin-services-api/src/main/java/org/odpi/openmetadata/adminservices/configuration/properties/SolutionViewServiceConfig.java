/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.configuration.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceRegistrationEntry;

import java.io.Serial;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * The SolutionViewServiceConfig class is a specialization of ViewServiceConfig for solution-level view services
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SolutionViewServiceConfig extends ViewServiceConfig
{
    /* There are no additional properties yet */

    /**
     * Default constructor for use with Jackson libraries
     */
    public SolutionViewServiceConfig()
    {
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
    public SolutionViewServiceConfig(ViewServiceRegistrationEntry viewRegistration)
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
        return "SolutionViewServiceConfig{} " + super.toString();
    }
}
