/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.viewservices.dino.api.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.adminservices.configuration.properties.AccessServiceConfig;
import org.odpi.openmetadata.adminservices.configuration.properties.IntegrationServiceConfig;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ServiceDetails {

    public enum ServiceCat {
        IntegrationService,
        AccessService
        // TODO - others to be added...
    }

    private static final long    serialVersionUID = 1L;


    private ServiceCat serviceCat;
    private IntegrationServiceConfig integrationServiceConfig;
    private AccessServiceConfig accessServiceConfig;


    /**
     * Default Constructor sets the properties to nulls
     */
    public ServiceDetails()
    {
        /*
         * Nothing to do.
         */
    }

    /**
     * Copy constructor
     * @param template
     */
    public ServiceDetails(ServiceDetails template) {

        this.integrationServiceConfig = template.getIntegrationServiceConfig();
        this.accessServiceConfig      = template.getAccessServiceConfig();

    }

    public IntegrationServiceConfig getIntegrationServiceConfig() {  return integrationServiceConfig;  }

    public void setIntegrationServiceConfig(IntegrationServiceConfig integrationServiceConfig) {
        this.integrationServiceConfig = integrationServiceConfig;
    }

    public AccessServiceConfig getAccessServiceConfig() {  return accessServiceConfig;  }

    public void setAccessServiceConfig(AccessServiceConfig accessServiceConfig) {
        this.accessServiceConfig = accessServiceConfig;
    }

    public ServiceCat getServiceCat() {  return serviceCat;  }

    public void setServiceCat(ServiceCat serviceCat) {
        this.serviceCat = serviceCat;
    }
}
