/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.glossaryview.server.admin;

import org.odpi.openmetadata.adminservices.registration.OMAGAccessServiceRegistration;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceRegistrationEntry;
import org.odpi.openmetadata.adminservices.configuration.registration.ServiceOperationalStatus;

public class GlossaryViewRegistration {

    private GlossaryViewRegistration() {}

    /*
     * Registers this OMAS in the access service registry
     */
    public static void registerAccessService() {
        AccessServiceDescription myDescription = AccessServiceDescription.GLOSSARY_VIEW_OMAS;

        AccessServiceRegistrationEntry myRegistration = new AccessServiceRegistrationEntry(myDescription,
                                                                                           ServiceOperationalStatus.ENABLED,
                                                                                           GlossaryViewAdmin.class.getName());

        OMAGAccessServiceRegistration.registerAccessService(myRegistration);
    }

}
