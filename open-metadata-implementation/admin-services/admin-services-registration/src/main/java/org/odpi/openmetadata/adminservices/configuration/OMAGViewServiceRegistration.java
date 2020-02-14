/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.configuration;

import org.odpi.openmetadata.adminservices.configuration.registration.ServiceRegistration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * OMAGViewServiceRegistration provides static methods to enable an view service to dynamically
 * register itself with the OMAG Server.
 *
 * Static methods are needed to be able to get this information to the Admin Services REST API singletons
 * created by Spring.
 */
public class OMAGViewServiceRegistration
{
    /*
     * A map is used so multiple registrations from the same view service are ignored.
     * The last registration is used.
     */
    static private Map<String, ServiceRegistration> viewServiceRegistrationMap = new HashMap<>();


    /**
     * Register OMVS
     *
     * @param registration information about the specific OMVS
     */
    public static synchronized void registerViewService(ServiceRegistration registration)
    {
        if (registration != null)
        {
            String  serviceName = registration.getServiceName();

            if (serviceName != null)
            {
                viewServiceRegistrationMap.put(serviceName, registration);
            }
        }
    }


    /**
     * Retrieve the list of registered OMVSs
     *
     * @return list of registration info
     */
    public static synchronized List<ServiceRegistration> getViewServiceRegistrationList()
    {
        List<ServiceRegistration>  registrationList = new ArrayList<>();

        for (ServiceRegistration viewServiceRegistration : viewServiceRegistrationMap.values())
        {
            if (viewServiceRegistration != null)
            {
                registrationList.add(viewServiceRegistration);
            }
        }

        return registrationList;
    }


    /**
     * Retrieve the requested OMVS registration information
     *
     * @param urlMarker URL insert that identifies the service
     * @return view service registration info
     */
    public static synchronized ServiceRegistration getViewServiceRegistration(String   urlMarker)
    {
        if (urlMarker != null)
        {
            for (ServiceRegistration viewServiceRegistration : viewServiceRegistrationMap.values())
            {
                if (viewServiceRegistration != null)
                {
                    if (urlMarker.equals(viewServiceRegistration.getServiceURLMarker()))
                    {
                        return viewServiceRegistration;
                    }
                }
            }
        }

        return null;
    }
}
