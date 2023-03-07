/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.viewservices.dino.api.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.adminservices.configuration.properties.AccessServiceConfig;
import org.odpi.openmetadata.adminservices.configuration.properties.EngineServiceConfig;
import org.odpi.openmetadata.adminservices.configuration.properties.IntegrationServiceConfig;
import org.odpi.openmetadata.adminservices.configuration.properties.ViewServiceConfig;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ServiceDetails {

    public enum ServiceCat {
        IntegrationService,
        EngineService,
        AccessService,
        ViewService
    }

    private static final long    serialVersionUID = 1L;


    private ServiceCat               serviceCat;
    private IntegrationServiceConfig integrationServiceConfig;
    private EngineServiceConfig      engineServiceConfig;
    private AccessServiceConfig      accessServiceConfig;
    private ViewServiceConfig        viewServiceConfig;


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
     * @param template object to copy
     */
    public ServiceDetails(ServiceDetails template) {

        this.integrationServiceConfig = template.getIntegrationServiceConfig();
        this.engineServiceConfig      = template.getEngineServiceConfig();
        this.accessServiceConfig      = template.getAccessServiceConfig();
        this.viewServiceConfig        = template.getViewServiceConfig();
    }

    public ServiceCat getServiceCat() {  return serviceCat;  }

    public void setServiceCat(ServiceCat serviceCat) {
        this.serviceCat = serviceCat;
    }

    public IntegrationServiceConfig getIntegrationServiceConfig() {  return integrationServiceConfig;  }

    public void setIntegrationServiceConfig(IntegrationServiceConfig integrationServiceConfig) {
        this.integrationServiceConfig = integrationServiceConfig;
    }

    public EngineServiceConfig getEngineServiceConfig() {  return engineServiceConfig;  }

    public void setEngineServiceConfig(EngineServiceConfig engineServiceConfig) {
        this.engineServiceConfig = engineServiceConfig;
    }

    public AccessServiceConfig getAccessServiceConfig() {  return accessServiceConfig;  }

    public void setAccessServiceConfig(AccessServiceConfig accessServiceConfig) {
        this.accessServiceConfig = accessServiceConfig;
    }

    public ViewServiceConfig getViewServiceConfig() {  return viewServiceConfig;  }

    public void setViewServiceConfig(ViewServiceConfig viewServiceConfig) {
        this.viewServiceConfig = viewServiceConfig;
    }


}
