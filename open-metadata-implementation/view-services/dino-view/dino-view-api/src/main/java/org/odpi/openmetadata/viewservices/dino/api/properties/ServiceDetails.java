/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.viewservices.dino.api.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.repositoryservices.connectors.stores.cohortregistrystore.properties.MemberRegistration;
import org.odpi.openmetadata.repositoryservices.properties.CohortDescription;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ServiceDetails {

    private static final long    serialVersionUID = 1L;


    private String serviceName;


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

        this.serviceName = template.getServiceName();



    }

    public String getServiceName() {  return serviceName;  }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }




}
