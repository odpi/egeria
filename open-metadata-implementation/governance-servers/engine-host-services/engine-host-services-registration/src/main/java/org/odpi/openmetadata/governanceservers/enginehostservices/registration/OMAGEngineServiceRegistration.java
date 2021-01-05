/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.enginehostservices.registration;

import org.odpi.openmetadata.adminservices.configuration.registration.EngineServiceRegistration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * OMAGEngineServiceRegistration provides static methods to enable an engine service to dynamically
 * register itself with the OMAG Server.
 *
 * Static methods are needed to be able to get this information to the Admin Services REST API singletons
 * created by Spring.
 */
public class OMAGEngineServiceRegistration
{
    /*
     * A map is used so multiple registrations from the same engine service are ignored.
     * The last registration is used.
     */
    static private Map<String, EngineServiceRegistration> engineServiceRegistrationMap = new HashMap<>();


    /**
     * Register Open Metadata Engine Service (OMES)
     *
     * @param registration information about the specific OMES
     */
    public static synchronized void registerEngineService(EngineServiceRegistration registration)
    {
        if (registration != null)
        {
            String  serviceName = registration.getEngineServiceName();

            if (serviceName != null)
            {
                engineServiceRegistrationMap.put(serviceName, registration);
            }
        }
    }


    /**
     * Retrieve the list of registered OMESs
     *
     * @return list of registration info
     */
    public static synchronized List<EngineServiceRegistration> getEngineServiceRegistrationList()
    {
        List<EngineServiceRegistration>  registrationList = new ArrayList<>();

        for (EngineServiceRegistration engineServiceRegistration : engineServiceRegistrationMap.values())
        {
            if (engineServiceRegistration != null)
            {
                registrationList.add(engineServiceRegistration);
            }
        }

        return registrationList;
    }


    /**
     * Retrieve the requested OMES registration information
     *
     * @param urlMarker URL insert that identifies the service
     * @return engine service registration info
     */
    public static synchronized EngineServiceRegistration getEngineServiceRegistration(String   urlMarker)
    {
        if (urlMarker != null)
        {
            for (EngineServiceRegistration engineServiceRegistration : engineServiceRegistrationMap.values())
            {
                if (engineServiceRegistration != null)
                {
                    if (urlMarker.equals(engineServiceRegistration.getEngineServiceURLMarker()))
                    {
                        return engineServiceRegistration;
                    }
                }
            }
        }

        return null;
    }
}
