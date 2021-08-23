/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.glossaryview.server.admin;

import org.odpi.openmetadata.adminservices.configuration.OMAGAccessServiceRegistration;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceRegistration;
import org.odpi.openmetadata.adminservices.configuration.registration.ServiceOperationalStatus;

public class GlossaryViewRegistration {

    private GlossaryViewRegistration() {}

    /*
     * Registers this OMAS in the access service registry
     */
    public static void registerAccessService() {
        AccessServiceDescription myDescription = AccessServiceDescription.GLOSSARY_VIEW_OMAS;

        AccessServiceRegistration myRegistration = new AccessServiceRegistration(myDescription,
                                                                                 ServiceOperationalStatus.ENABLED,
                                                                                 GlossaryViewAdmin.class.getName());

        OMAGAccessServiceRegistration.registerAccessService(myRegistration);
    }

}
