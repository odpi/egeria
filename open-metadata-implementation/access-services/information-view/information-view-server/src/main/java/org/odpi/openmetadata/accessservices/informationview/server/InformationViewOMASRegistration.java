/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.informationview.server;


import org.odpi.openmetadata.accessservices.informationview.admin.InformationViewAdmin;
import org.odpi.openmetadata.adminservices.configuration.OMAGAccessServiceRegistration;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.adminservices.configuration.registration.ServiceOperationalStatus;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceRegistration;

class InformationViewOMASRegistration {

    InformationViewOMASRegistration() {

        AccessServiceDescription myDescription = AccessServiceDescription.INFORMATION_VIEW_OMAS;
        AccessServiceRegistration myRegistration = new AccessServiceRegistration(myDescription,
                                                                                 ServiceOperationalStatus.DISABLED,
                                                                                 InformationViewAdmin.class.getName()

        );
        OMAGAccessServiceRegistration.registerAccessService(myRegistration);
    }
}
