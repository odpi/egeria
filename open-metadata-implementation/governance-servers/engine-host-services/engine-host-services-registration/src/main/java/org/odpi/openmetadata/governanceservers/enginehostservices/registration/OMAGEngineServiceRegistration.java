/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.enginehostservices.registration;

import org.odpi.openmetadata.adminservices.configuration.registration.EngineServiceRegistrationEntry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * OMAGEngineServiceRegistration provides static methods to enable an engine service to dynamically
 * register itself with the OMAG Server.
 * Static methods are needed to be able to get this information to the Admin Services REST API singletons
 * created by Spring.
 */
public class OMAGEngineServiceRegistration
{
    /*
     * A map is used so multiple registrations from the same engine service are ignored.
     * The last registration is used.
     */
    static final private Map<String, EngineServiceRegistrationEntry> engineServiceRegistrationMap = new HashMap<>();
    static final private Map<String, GovernanceEngineHandlerFactory> engineHandlerFactoryMap = new HashMap<>();
    static final private Map<String, String> engineServiceToTypeNameMap = new HashMap<>();


    /**
     * Register Open Metadata Engine Service (OMES)
     *
     * @param registration information about the specific OMES
     * @param engineHandlerFactory factory object for the engine service
     */
    public static synchronized void registerEngineService(EngineServiceRegistrationEntry registration,
                                                          GovernanceEngineHandlerFactory engineHandlerFactory)
    {
        if (registration != null)
        {
            String  serviceName = registration.getEngineServiceName();

            if (serviceName != null)
            {
                engineServiceRegistrationMap.put(serviceName, registration);
            }

            String governanceEngineTypeName = registration.getHostedGovernanceEngineType();

            if (governanceEngineTypeName != null)
            {
                engineHandlerFactoryMap.put(governanceEngineTypeName, engineHandlerFactory);
                engineServiceToTypeNameMap.put(registration.getEngineServiceURLMarker(),
                                               governanceEngineTypeName);
            }
        }
    }


    /**
     * Retrieve the list of registered OMESs
     *
     * @return list of registration info
     */
    public static synchronized List<EngineServiceRegistrationEntry> getEngineServiceRegistrationList()
    {
        List<EngineServiceRegistrationEntry> registrationList = new ArrayList<>();

        for (EngineServiceRegistrationEntry engineServiceRegistration : engineServiceRegistrationMap.values())
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
    public static synchronized EngineServiceRegistrationEntry getEngineServiceRegistration(String   urlMarker)
    {
        if (urlMarker != null)
        {
            for (EngineServiceRegistrationEntry engineServiceRegistration : engineServiceRegistrationMap.values())
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


    /**
     * Return the type name of the governance engine for the url marker for the engine service.
     *
     * @param serviceURLMarker url marker for the engine service
     * @return open metadata type name
     */
    public static String getGovernanceEngineTypeName(String serviceURLMarker)
    {
        return engineServiceToTypeNameMap.get(serviceURLMarker);
    }


    /**
     * Retrieve the requested OMES governance engine handler factory map
     *
     * @return engine service registration info
     */
    public static synchronized Map<String, GovernanceEngineHandlerFactory> getGovernanceEngineHandlerFactoryMap()
    {
        return new HashMap<>(engineHandlerFactoryMap);
    }
}
