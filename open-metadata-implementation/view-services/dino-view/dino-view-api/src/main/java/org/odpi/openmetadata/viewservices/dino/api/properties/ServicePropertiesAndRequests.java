/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.viewservices.dino.api.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.governanceengine.properties.GovernanceServiceProperties;

import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ServicePropertiesAndRequests {

    private static final long    serialVersionUID = 1L;

    private GovernanceServiceProperties       serviceProperties;
    private Map<String, Map<String, String>>  requestTypeParameters;


    /**
     * Default Constructor sets the properties to nulls
     */
    public ServicePropertiesAndRequests()
    {
        /*
         * Nothing to do.
         */
    }

    public ServicePropertiesAndRequests(GovernanceServiceProperties       serviceProperties,
                                        Map<String, Map<String, String>>  requestTypeParameters)
        {

        this.serviceProperties              = serviceProperties;
        this.requestTypeParameters          = requestTypeParameters;

    }


    public GovernanceServiceProperties getServiceProperties() {
        return serviceProperties;
    }

    public void setServiceProperties(GovernanceServiceProperties serviceProperties) {
        this.serviceProperties = serviceProperties;
    }

    public  Map<String, Map<String, String>> getRequestTypeParameters() { return requestTypeParameters;  }

    public void setRequestTypeParameters( Map<String, Map<String, String>> requestTypeParameters) {
        this.requestTypeParameters = requestTypeParameters;
    }



    /**
     * Standard toString method.
     *
     * @return JSON style description of variables.
     */
    @Override
    public String toString()
    {
        return "ServicePropertiesAndRequests{" +
                ", serviceProperties='" + serviceProperties + '\'' +
                ", requestTypeParameters='" + requestTypeParameters + '\'' +
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
        ServicePropertiesAndRequests that = (ServicePropertiesAndRequests) objectToCompare;
        return Objects.equals(getRequestTypeParameters(), that.getRequestTypeParameters()) &&
               Objects.equals(getServiceProperties(), that.getServiceProperties());
     }


    /**
     * Return a hash code based on the values of this object.
     *
     * @return in hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(getServiceProperties(), getRequestTypeParameters() );
    }

}
