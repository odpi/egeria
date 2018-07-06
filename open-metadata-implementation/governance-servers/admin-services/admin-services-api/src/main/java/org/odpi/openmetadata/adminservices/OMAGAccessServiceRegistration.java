/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.adminservices;

import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceRegistration;

import java.util.ArrayList;
import java.util.List;


/**
 * OMAGAccessServiceRegistration provides static methods for an access service to
 * register itself with the OMAG Server.  Static methods are needed to be able to
 * get this information to the Admin Services REST API singletons created by Spring.
 */
public class OMAGAccessServiceRegistration
{
    static private List<AccessServiceRegistration> accessServiceRegistrationList = new ArrayList<>();


    /**
     * Register OMAS
     *
     * @param registration information about the specific OMAS
     */
    public static synchronized void registerAccessService(AccessServiceRegistration registration)
    {
        if (registration != null)
        {
            accessServiceRegistrationList.add(registration);
        }
    }


    /**
     * Retrieve the list of registered OMASs
     *
     * @return list of registration info
     */
    public static synchronized  List<AccessServiceRegistration> getAccessServiceRegistrationList()
    {
        return accessServiceRegistrationList;
    }

}
