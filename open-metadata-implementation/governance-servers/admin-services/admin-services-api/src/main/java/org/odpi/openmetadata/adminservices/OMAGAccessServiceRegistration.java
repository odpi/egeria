/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices;

import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceRegistration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * OMAGAccessServiceRegistration provides static methods for an access service to
 * register itself with the OMAG Server.  Static methods are needed to be able to
 * get this information to the Admin Services REST API singletons created by Spring.
 */
public class OMAGAccessServiceRegistration
{
    /*
     * A map is used so multiple registrations from the same access service are ignored.
     * The last registration is used.
     */
    static private Map<String, AccessServiceRegistration> accessServiceRegistrationMap = new HashMap<>();


    /**
     * Register OMAS
     *
     * @param registration information about the specific OMAS
     */
    public static synchronized void registerAccessService(AccessServiceRegistration registration)
    {
        if (registration != null)
        {
            String  serviceName = registration.getAccessServiceName();

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
    public static synchronized  List<AccessServiceRegistration> getAccessServiceRegistrationList()
    {
        List<AccessServiceRegistration>  registrationList = new ArrayList<>();

        for (AccessServiceRegistration   accessServiceRegistration : accessServiceRegistrationMap.values())
        {
            if (accessServiceRegistration != null)
            {
                registrationList.add(accessServiceRegistration);
            }
        }

        return registrationList;
    }

}
