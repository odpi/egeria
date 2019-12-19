/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.common.registration;

import org.odpi.openmetadata.userinterface.adminservices.configuration.registration.ViewServiceRegistration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * OMAGViewServiceRegistration provides static methods to enable an access service to dynamically
 * register itself with the UI Server.
 *
 * Static methods are needed to be able to get this information to the Admin Services REST API singletons
 * created by Spring.
 */
public class OMAGViewServiceRegistration
{
    /*
     * A map is used so multiple registrations from the same access service are ignored.
     * The last registration is used.
     */
    static private Map<String, ViewServiceRegistration> viewServiceRegistrationMap = new HashMap<>();


    /**
     * Register Open Metadata View service (OMVS)
     *
     * @param registration information about the specific OMVS
     */
    public static synchronized void registerViewService(ViewServiceRegistration registration)
    {
        if (registration != null)
        {
            String  serviceName = registration.getViewServiceName();

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
    public static synchronized List<ViewServiceRegistration> getViewServiceRegistrationList()
    {
        List<ViewServiceRegistration>  registrationList = new ArrayList<>();

        for (ViewServiceRegistration accessServiceRegistration : viewServiceRegistrationMap.values())
        {
            if (accessServiceRegistration != null)
            {
                registrationList.add(accessServiceRegistration);
            }
        }

        return registrationList;
    }


    /**
     * Retrieve the requested OMVS registration information
     *
     * @param urlMarker URL insert that identifies the service
     * @return access service registration info
     */
    public static synchronized ViewServiceRegistration getViewServiceRegistration(String   urlMarker)
    {
        if (urlMarker != null)
        {
            for (ViewServiceRegistration viewServiceRegistration : viewServiceRegistrationMap.values())
            {
                if (viewServiceRegistration != null)
                {
                    if (urlMarker.equals(viewServiceRegistration.getViewServiceURLMarker()))
                    {
                        return viewServiceRegistration;
                    }
                }
            }
        }

        return null;
    }
}
