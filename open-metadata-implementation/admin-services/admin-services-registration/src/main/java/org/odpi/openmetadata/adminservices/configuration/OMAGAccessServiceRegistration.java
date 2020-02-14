/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.configuration;

import org.odpi.openmetadata.adminservices.configuration.registration.ServiceRegistration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * OMAGAccessServiceRegistration provides static methods to enable an access service to dynamically
 * register itself with the OMAG Server.
 *
 * Static methods are needed to be able to get this information to the Admin Services REST API singletons
 * created by Spring.
 */
public class OMAGAccessServiceRegistration
{
    /*
     * A map is used so multiple registrations from the same access service are ignored.
     * The last registration is used.
     */
    static private Map<String, ServiceRegistration> accessServiceRegistrationMap = new HashMap<>();


    /**
     * Register OMAS
     *
     * @param registration information about the specific OMAS
     */
    public static synchronized void registerAccessService(ServiceRegistration registration)
    {
        if (registration != null)
        {
            String  serviceName = registration.getServiceName();

            if (serviceName != null)
            {
                accessServiceRegistrationMap.put(serviceName, registration);
            }
        }
    }


    /**
     * Retrieve the list of registered OMASs
     *
     * @return list of registration info
     */
    public static synchronized List<ServiceRegistration> getAccessServiceRegistrationList()
    {
        List<ServiceRegistration>  registrationList = new ArrayList<>();

        for (ServiceRegistration accessServiceRegistration : accessServiceRegistrationMap.values())
        {
            if (accessServiceRegistration != null)
            {
                registrationList.add(accessServiceRegistration);
            }
        }

        return registrationList;
    }


    /**
     * Retrieve the requested OMAS registration information
     *
     * @param urlMarker URL insert that identifies the service
     * @return access service registration info
     */
    public static synchronized ServiceRegistration getAccessServiceRegistration(String   urlMarker)
    {
        if (urlMarker != null)
        {
            for (ServiceRegistration accessServiceRegistration : accessServiceRegistrationMap.values())
            {
                if (accessServiceRegistration != null)
                {
                    if (urlMarker.equals(accessServiceRegistration.getServiceURLMarker()))
                    {
                        return accessServiceRegistration;
                    }
                }
            }
        }

        return null;
    }
}
